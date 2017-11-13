package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharsetTestCase {

    @Test
    public void testNotSatisfies() {
        final Charset charset = new Charset("utf-8");
        assertFalse(charset.satisfies("utf-16"));
    }

    @Test
    public void testExactSatisfies() {
        final Charset charset = new Charset("utf-8");
        assertTrue(charset.satisfies("utf-8"));
    }

    @Test
    public void testWildSatisfies() {
        final Charset charset = new Charset("*");
        assertTrue(charset.satisfies("gzip"));
    }

}
