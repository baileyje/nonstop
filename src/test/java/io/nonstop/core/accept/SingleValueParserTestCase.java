package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleValueParserTestCase {

    class Thing extends Resolvable {
        String value;

        public Thing(final String value, final float weight, final int index) {
            super(weight, index);
            this.value = value;
        }

        @Override
        public boolean satisfies(String value) {
            return false;
        }
    }

    private final SingleValueParser<Thing> parser = new SingleValueParser<Thing>() {
        @Override
        Thing create(String value, float weight, int index) {
            return new Thing(value, weight, index);
        }
    };

    @Test
    public void testNoWeight() {
        final Thing thing = parser.parse("gzip");
        assertEquals("gzip", thing.value);
        assertEquals(1.0f, thing.getWeight(), 1.0);
        assertEquals(0, thing.getIndex());
    }

    @Test
    public void testWithWeight() {
        final Thing thing = parser.parse("gzip;q=0.8");
        assertEquals("gzip", thing.value);
        assertEquals(0.8f, thing.getWeight(), 0.1);
        assertEquals(0, thing.getIndex());
    }

    @Test
    public void testWild() {
        final Thing thing = parser.parse("*");
        assertEquals("*", thing.value);
    }

}
