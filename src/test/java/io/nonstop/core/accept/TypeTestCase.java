package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test accept Type object.
 *
 * @author John Bailey
 */
public class TypeTestCase {

    @Test
    public void testNotSatisfies() {
        final Type type = new Type("application", "json");
        assertFalse(type.satisfies("text/html"));
    }

    @Test
    public void testExactSatisfies() {
        final Type type = new Type("application", "json");
        assertTrue(type.satisfies("application/json"));
    }

    @Test
    public void testWildSubtypeNotSatisfies() {
        final Type type = new Type("application", "*");
        assertFalse(type.satisfies("text/html"));
    }

    @Test
    public void testWildSubtypeSatisfies() {
        final Type type = new Type("application", "*");
        assertTrue(type.satisfies("application/json"));
    }

    @Test
    public void testWildSatisfies() {
        final Type type = new Type("*", "*");
        assertTrue(type.satisfies("application/json"));
    }
}
