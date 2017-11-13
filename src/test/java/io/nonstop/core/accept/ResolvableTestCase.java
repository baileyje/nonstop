package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test Resolvable base class.
 *
 * @author John Bailey
 */
public class ResolvableTestCase {


    @Test
    public void testCompareToByWeight() {
        final Type specific = new Type("application", "json");
        final Type wild = new Type("*", "*", 0.8f, 0);
        assertEquals(-1, specific.compareTo(wild));
        assertEquals(1, wild.compareTo(specific));
        assertEquals(0, specific.compareTo(specific));
    }

    @Test
    public void testCompareToByIndex() {
        final Type one = new Type("text", "html", 1.0f, 0);
        final Type two = new Type("application", "json", 1.0f, 1);
        assertEquals(-1, one.compareTo(two));
        assertEquals(1, two.compareTo(one));
    }

}
