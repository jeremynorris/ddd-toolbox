package org.ddd.toolbox.hibernate.typemap;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Hibernate type to hold a Set of Integers.
 *
 * <br/>
 * Patterns:
 * <p/>
 * <br/>
 * Revisions:
 * david.krisch: Feb 24, 2009: Initial revision.
 *
 * @author david.krisch
 */
public class SetOfIntegersType implements UserType {

    public static final String SEPARATOR = ",";

    /**
     * @frameworkUseOnly
     */
    public SetOfIntegersType() {
    }

    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }

    /**
     * {@inheritDoc}
     */
    public Class returnedClass() {
        return Set.class;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        if (!(x instanceof Set) || !(y instanceof Set)) {
            return false;
        }

        return x.equals(y) && y.equals(x);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {

        Object result;

        if (rs == null) {
            result = null;
        }
        else {
            result = getSetFromString((String) rs.getObject(names[0]));
        }

        return result;
    }

    /**
     * Convert a String of SEPARATOR delimited Integers into  a Set
     *
     * @param str string of SEPARATOR delimited Integers
     * @return a Set of Integers
     */
    public static Set<Integer> getSetFromString(String str) {
        Set<Integer> result = new TreeSet<Integer>();

        if (str == null) {
            return null;
        }
        else if (str.length() != 0) {
            String[] nums = str.split(SEPARATOR);

            for (String num : nums) {
                result.add(new Integer(num));
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        }
        else {
            st.setString(index, getStringFromSet((Set<Integer>) value));
        }
    }

    /**
     * Convert a set of integers into a string representing the set
     *
     * @param set the <code>Set</code> of integers to be converted
     * @return a String of the integers from set separated by SEPARATOR
     */
    public static String getStringFromSet(Set<Integer> set) {
        return StringUtils.join(set, SEPARATOR);
    }

    /**
     * {@inheritDoc}
     */
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMutable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }

    /**
     * {@inheritDoc}
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    /**
     * {@inheritDoc}
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
