package org.ddd.toolbox.hibernate.typemap;

import java.io.File;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class FileType implements UserType
{
    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable()
    {
        return false;
    }
	
    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException
    {
        // File objects are immutable: a deep copy is a noop.
        return value;
    }
    
    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
	public Class returnedClass()
	{
		return File.class;
	}

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes()
    {
        return new int[] { Types.VARCHAR };
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#equals(Object, Object) 
     */
    public boolean equals(Object x, Object y) throws HibernateException
    {
        if (x == y) 
        {
            return true;
        }

        if (x == null || y == null) 
        {
            return false;
        }

        if (!(x instanceof File) || !(y instanceof File)) 
        {
            return false;
        }

        return x.equals(y) && y.equals(x);
    }
    
    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }
    
    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
     *      java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException
    {
        return new File((String) rs.getObject(names[0]));
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException
    {
        if (value == null)
        {
            st.setNull(index, Types.VARCHAR);
        }
        else 
        {
        	File valueFile = (File) value;
        	st.setString(index, valueFile.getPath());
        }
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
     *      java.lang.Object)
     */
    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
        return cached;
    }

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
    }
}
