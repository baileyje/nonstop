package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeParserTestCase {

    private final TypeParser parser = new TypeParser();

    @Test
    public void testNoWeight() {
        final Type type = parser.parse("application/json");
        assertEquals("application", type.getType());
        assertEquals("json", type.getSubtype());
        assertEquals(1.0f, type.getWeight(), 1.0);
        assertEquals(0, type.getIndex());
    }

    @Test
    public void testWithWeight() {
        final Type type = parser.parse("application/json;q=0.8");
        assertEquals("application", type.getType());
        assertEquals("json", type.getSubtype());
        assertEquals(0.8f, type.getWeight(), 0.1);
        assertEquals(0, type.getIndex());
    }

    @Test
    public void testWildSubtype() {
        final Type type = parser.parse("application/*");
        assertEquals("application", type.getType());
        assertEquals("*", type.getSubtype());
    }

    @Test
    public void testWild() {
        final Type type = parser.parse("*/*");
        assertEquals("*", type.getType());
        assertEquals("*", type.getSubtype());
    }

}
