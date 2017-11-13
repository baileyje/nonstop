package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EncodingTestCase {

    @Test
    public void testNotSatisfies() {
        final Encoding encoding = new Encoding("gzip");
        assertFalse(encoding.satisfies("compress"));
    }

    @Test
    public void testExactSatisfies() {
        final Encoding encoding = new Encoding("gzip");
        assertTrue(encoding.satisfies("gzip"));
    }

    @Test
    public void testWildSatisfies() {
        final Encoding encoding = new Encoding("*");
        assertTrue(encoding.satisfies("gzip"));
    }

}
