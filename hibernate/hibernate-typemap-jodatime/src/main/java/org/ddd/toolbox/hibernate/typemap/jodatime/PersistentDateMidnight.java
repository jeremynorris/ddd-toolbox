package org.ddd.toolbox.hibernate.typemap.jodatime;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;
import org.joda.time.DateMidnight;

/**
 * PersistentDateTime
 *
 * <br>
 * Patterns:
 * 
 * <br>
 * Revisions:
 * jnorris: Dec 5, 2007: Initial revision.
 *
 * @author jnorris
 */
public class PersistentDateMidnight implements EnhancedUserType {
    
    private static final int[] SQL_TYPES = new int[] {
        Types.DATE,
    };

    /**
     * @frameworkUseOnly
     */
    public PersistentDateMidnight() {
    }
    
    /**
     * {@inheritDoc}
     */
    public int[] sqlTypes() {
        return SQL_TYPES.clone();
    }

    /**
     * {@inheritDoc}
     */
    public Class<DateMidnight> returnedClass() {
        return DateMidnight.class;
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
        DateMidnight dateMidnightx = (DateMidnight) x;
        DateMidnight dateMidnighty = (DateMidnight) y;

        return dateMidnightx.equals(dateMidnighty);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode(Object object) throws HibernateException {
        return object.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public Object nullSafeGet(ResultSet resultSet, String[] strings, Object object) throws HibernateException, SQLException {
        return nullSafeGet(resultSet, strings[0]);
    }

    /**
     * {@inheritDoc}
     */
    public Object nullSafeGet(ResultSet resultSet, String string) throws SQLException {
        Object date = Hibernate.DATE.nullSafeGet(resultSet, string);
        if (date == null) {
            return null;
        }
        return new DateMidnight(date);
    }

    /**
     * {@inheritDoc}
     */
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            Hibernate.DATE.nullSafeSet(preparedStatement, null, index);
        }
        else {
            Hibernate.DATE.nullSafeSet(preparedStatement, ((DateMidnight) value).toDate(), index);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        return new DateMidnight(value);
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
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     */
    public Object assemble(Serializable cached, Object value) throws HibernateException {
        return cached;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    /**
     * {@inheritDoc}
     */
    public String objectToSQLString(Object object) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public String toXMLString(Object object) {
        return object.toString();
    }

    /**
     * {@inheritDoc}
     */
    public Object fromXMLString(String string) {
        return new DateMidnight(string);
    }
}
