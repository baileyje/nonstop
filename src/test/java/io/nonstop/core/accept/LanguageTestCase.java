package io.nonstop.core.accept;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LanguageTestCase {

    @Test
    public void testNotSatisfies() {
        final Language language = new Language("fr");
        assertFalse(language.satisfies("en"));
    }

    @Test
    public void testExactSatisfies() {
        final Language language = new Language("fr");
        assertTrue(language.satisfies("fr"));
    }

    @Test
    public void testWildSatisfies() {
        final Language language = new Language("*");
        assertTrue(language.satisfies("en"));
    }

}
