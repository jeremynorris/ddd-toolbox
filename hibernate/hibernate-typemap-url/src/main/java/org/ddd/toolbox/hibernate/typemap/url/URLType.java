package org.ddd.toolbox.hibernate.typemap.url;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * URLType
 *
 * <br>
 * Patterns:
 * 
 * <br>
 * Revisions:
 * jeremy.norris: Mar 29, 2007: Initial revision.
 *
 * @author jeremy.norris
 */
public class URLType implements CompositeUserType {

    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;

    /**
     * @frameworkUseOnly
     */
    public URLType() {
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
    public Class<URL> returnedClass() {
        return URL.class;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
        return cached;
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
    public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     */    
    public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
        return original;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) 
        {
            return true;
        }
        if (x == null || y == null) 
        {
            return false;
        }
        if (!(x instanceof URL) || !(y instanceof URL)) {
            return false;
        }

        return x.equals(y) && y.equals(x);
    }
    
    /**
     * {@inheritDoc}
     */
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    public String[] getPropertyNames() {
        return new String[] { "externalForm" };
    }

    /**
     * {@inheritDoc}
     */
    public Type[] getPropertyTypes() {
        return new Type[] { Hibernate.STRING };
    }

    /**
     * {@inheritDoc}
     */
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        URL url = (URL) component;
        return url.toExternalForm();
    }

    /**
     * {@inheritDoc}
     */
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        throw new UnsupportedOperationException("Immutable " + URL.class.getSimpleName());
    }

    /**
     * {@inheritDoc}
     */    
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String data = rs.getString(names[0]);
        if (data == null) {
            return null;
        }
        try {
            return new URL(data);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */    
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Hibernate.STRING.sqlType());
        }
        else {
            st.setString(index, ((URL) value).toExternalForm());
        }
    }
    
}
