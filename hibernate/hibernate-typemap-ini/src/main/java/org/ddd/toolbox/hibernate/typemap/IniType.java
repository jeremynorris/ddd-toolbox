package org.ddd.toolbox.hibernate.typemap;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.ini4j.Ini;

/**
 * IniType
 *
 * <br>
 * Patterns:
 * 
 * <br>
 * Revisions:
 * jeremy.norris: Mar 28, 2007: Initial revision.
 *
 * @author jeremy.norris
 */
public class IniType implements UserType {

    /**
     * @frameworkUseOnly
     */
    public IniType() {
    }
    
    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException {
        // According to the UserType API documentation, the deep copy should
        // stop at entities and collections.  Ini extends a collection.
        return value;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class<Ini> returnedClass() {
        return Ini.class;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes() {
        return new int[] { Types.CLOB };
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#equals()
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        
        if (x == y) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        if (!(x instanceof Ini) || !(y instanceof Ini)) {
            return false;
        }

        return x.equals(y) && y.equals(x);
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
     *      java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        
        Ini result = null;

        Clob clob = rs.getClob(names[0]);

        if (clob != null) {
            try {
                result = new Ini(clob.getCharacterStream());
            }
            catch (IOException e) {
                throw new HibernateException(e);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        
        if (value == null) {
            st.setNull(index, Types.CLOB);
        }
        else {
            Ini ini = (Ini) value;
            StringWriter writer = null;
            try {
                writer = new StringWriter();
                ini.store(writer);
            }
            catch (IOException e) {
                throw new HibernateException(e);
            }
            finally {
                IOUtils.closeQuietly(writer);
            }

            // Note: Unable to stream this to the clob because all the
            // interfaces (as of Java 5) require the number of characters being
            // streamed which seems to defeat one of the main purposes of
            // streaming. Perhaps this is a architectural issue where the JDBC
            // driver needs to know the number of bytes being written at this
            // level of the API. Therefore, string slurping instead.
            
            String content = writer.getBuffer().toString();
            StringReader reader = new StringReader(content);
            st.setCharacterStream(index, reader, content.length());
        }
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
     *      java.lang.Object)
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
