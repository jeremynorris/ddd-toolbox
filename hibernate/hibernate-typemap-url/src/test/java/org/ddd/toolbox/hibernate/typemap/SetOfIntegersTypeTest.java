package org.ddd.toolbox.hibernate.typemap;

import static org.testng.Assert.assertEquals;

import java.util.Set;
import java.util.TreeSet;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * <br/>
 * Patterns:
 * <p/>
 * <br/>
 * Revisions:
 * david.krisch: Feb 24, 2009: Initial revision.
 *
 * @author david.krisch
 */
public class SetOfIntegersTypeTest {

    @DataProvider(name = "setProvider")
    public Object[][] setDataProvider() {
        Object[][] result = new Object[4][3];

        result[0] = new Object[] {null, null, "Test with null set failed."};

        result[1] = new Object[] {"", new TreeSet<Integer>(), "Test with no elements failed."};

        Set<Integer> set = new TreeSet<Integer>();
        set.add(1);
        result[2] = new Object[] {"1", set, "Test with one element failed."};

        set = new TreeSet<Integer>();
        set.add(1); set.add(2); set.add(3);
        result[3] = new Object[] {"1,2,3", set, "Test with three elements failed."};

        return result;
    }

    @Test(dataProvider = "setProvider", groups = "FUNCTIONAL")
    public void testGetStringFromSet(String expected, Set<Integer> input, String msg) {
        assertEquals(expected, SetOfIntegersType.getStringFromSet(input), msg);    
    }

    @Test(dataProvider = "setProvider", groups = "FUNCTIONAL")
    public void testGetSetFromString(String input, Set<Integer> expected, String msg) {
       assertEquals(expected, SetOfIntegersType.getSetFromString(input), msg);       
   }
}
