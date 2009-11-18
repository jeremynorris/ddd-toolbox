/**
 * 
 */
package org.ddd.toolbox.hibernate.typemap.uuid;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Formatter;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;

/**
 * @author cjtucker
 */
public class JavaUtilUUIDType implements EnhancedUserType, Serializable
{

    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an empty instance (required by hibernate spec).
     * 
     */
    public JavaUtilUUIDType()
    {
        // No op
    }

    /**
     * @see org.hibernate.usertype.EnhancedUserType#objectToSQLString(java.lang.Object)
     */
    public String objectToSQLString(Object value)
    {

        UUID uuid = (UUID) value;

        // Convert UUID to a byte array, and return this as a string converted
        // using the CharBuffer.
        byte[] bytes = uuidToByteArray(uuid);
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return "0x" + formatter.out().toString();

    }

    /**
     * Convert a UUID to a byte array.
     * 
     * @param uuid UUID to convert
     * @return a byte array representing the bits from the UUID
     */
    byte[] uuidToByteArray(UUID uuid)
    {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        // Create a byte array and wrap it with a ByteBuffer
        byte[] bArray = new byte[16];
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);

        // Get a view of the byte buffer as a long buffer and put our two longs
        // into it
        LongBuffer lBuffer = bBuffer.asLongBuffer();
        lBuffer.put(msb);
        lBuffer.put(lsb);

        // Return the wrapped array
        return bArray;

    }

    /**
     * Conver a byte array to a UUID.
     * 
     * @param bArray 128 bit (16 element) byte array to convert
     * @return a UUID instantiated by the 128 bits of data in the byte array
     * @throws IllegalArgumentException if the byte array is not 128 bits long
     */
    UUID byteArrayToUuid(byte[] bArray)
    {
        if (bArray.length != 16) {
            throw new IllegalArgumentException(
                                               "Byte array is not 128 bits long");
        }

        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        LongBuffer lBuffer = bBuffer.asLongBuffer();

        long msb = lBuffer.get();
        long lsb = lBuffer.get();

        return new UUID(msb, lsb);
    }

    /**
     * @see org.hibernate.usertype.EnhancedUserType#toXMLString(java.lang.Object)
     */
    public String toXMLString(Object value)
    {
        return ((UUID) value).toString();
    }

    /**
     * @see org.hibernate.usertype.EnhancedUserType#fromXMLString(java.lang.String)
     */
    public Object fromXMLString(String xmlValue)
    {
        return UUID.fromString(xmlValue);
    }

    /**
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    public int[] sqlTypes()
    {
        return new int[]
        { Types.BINARY };
    }

    /**
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    public Class returnedClass()
    {
        return UUID.class;
    }

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object,
     *      java.lang.Object)
     */
    public boolean equals(Object x, Object y) throws HibernateException
    {
        if (x == y) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        if (!(x instanceof UUID) || !(y instanceof UUID)) {
            return false;
        }

        return x.equals(y) && y.equals(x);
    }

    /**
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet,
     *      java.lang.String[], java.lang.Object)
     */
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
        throws HibernateException, SQLException
    {
        byte[] result = (byte[]) rs.getObject(names[0]);
        if (null == result) {
            return null;
        }
        return byteArrayToUuid(result);
    }

    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement,
     *      java.lang.Object, int)
     */
    public void nullSafeSet(PreparedStatement st, Object value, int index)
        throws HibernateException, SQLException
    {
        if (null == value) {
            st.setNull(index, Types.BINARY);
        }
        else {
            st.setBytes(index, uuidToByteArray((UUID) value));
        }
    }

    /**
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    public Object deepCopy(Object value) throws HibernateException
    {
        UUID uuid = (UUID) value;
        return new UUID(uuid.getMostSignificantBits(),
                        uuid.getLeastSignificantBits());
    }

    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    public boolean isMutable()
    {
        return false;
    }

    /**
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    /**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable,
     *      java.lang.Object)
     */
    public Object assemble(Serializable cached, Object owner)
        throws HibernateException
    {
        return cached;
    }

    /**
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object replace(Object original, Object target, Object owner)
        throws HibernateException
    {
        return original;
    }

}
