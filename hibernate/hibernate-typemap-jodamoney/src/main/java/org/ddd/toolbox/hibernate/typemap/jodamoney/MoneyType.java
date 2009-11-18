package org.ddd.toolbox.hibernate.typemap.jodamoney;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.joda.money.Money;

public class MoneyType implements UserType
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
		return Money.class;
	}

    /**
     * {@inheritDoc}
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes()
    {
        return new int[] {
            Types.VARCHAR,  // ISO 4217 Currency Code
            Types.BIGINT    // CurrencyUnit: long iAmount
        }; 
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

        if (!(x instanceof Money) || !(y instanceof Money)) 
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
        String currencyCode = rs.getString(names[0]);
        long amount = rs.getLong(names[1]);
        return Money.ofMinor(currencyCode, amount);
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
            st.setNull(index + 1, Types.BIGINT);
        }
        else 
        {
        	Money money = (Money) value;
        	String currencyCode = money.getCurrencyUnit().getCurrencyCode();
        	long amount = money.getAmountMinor();
        	st.setString(index, currencyCode);
        	st.setLong(index + 1, amount);
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
