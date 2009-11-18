package org.ddd.toolbox.hibernate.typemap.uuid;

import java.util.UUID;

import org.ddd.toolbox.hibernate.typemap.uuid.JavaUtilUUIDType;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author cjtucker
 */
public class JavaUtilUUIDTypeTest extends TestCase
{

    UUID u1, u2;
    JavaUtilUUIDType cnv;
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        u1 = new UUID(0x0000FFFF0000FFFFL, 0x1111AAAA1111AAAAL);
        u2 = new UUID(0x1234567890ABCDEFL, 0xFEDCBA9876543210L);
        
        cnv = new JavaUtilUUIDType();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testObjectToSQLString()
    {
        Assert.assertEquals("0x0000ffff0000ffff1111aaaa1111aaaa", cnv.objectToSQLString(u1));
        Assert.assertEquals("0x1234567890abcdeffedcba9876543210", cnv.objectToSQLString(u2));
    }
    
    public void testRoundtrip()
    {
        Assert.assertEquals(u1, cnv.byteArrayToUuid(cnv.uuidToByteArray(u1)));
        Assert.assertEquals(u2, cnv.byteArrayToUuid(cnv.uuidToByteArray(u2)));
        Assert.assertNotSame(u1, cnv.byteArrayToUuid(cnv.uuidToByteArray(u2)));
        Assert.assertNotSame(u2, cnv.byteArrayToUuid(cnv.uuidToByteArray(u1)));
    }
}
