package io.nonstop.core.accept;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the WeightedResolver.
 */
public class WeightedResolverTestCase {

    @Test
    public void testSingleHeaderSingleValue() {
        final List<Type> resolved = WeightedResolver.resolve(Collections.singletonList("application/json"), Type.parser);
        assertEquals(1, resolved.size());
        assertEquals("application/json", resolved.get(0).toString());
    }

    @Test
    public void testSingleHeaderMultiValue() {
        final List<Type> resolved = WeightedResolver.resolve(Collections.singletonList("application/json,text/html"), Type.parser);
        assertEquals(2, resolved.size());
        assertEquals("application/json", resolved.get(0).toString());
        assertEquals("text/html", resolved.get(1).toString());
    }

    @Test
    public void testMultiHeaderSingleValue() {
        final List<Type> resolved = WeightedResolver.resolve(Arrays.asList("application/json", "text/html"), Type.parser);
        assertEquals(2, resolved.size());
        assertEquals("application/json", resolved.get(0).toString());
        assertEquals("text/html", resolved.get(1).toString());
    }

    @Test
    public void testMultiHeaderMultiValue() {
        final List<Type> resolved = WeightedResolver.resolve(Arrays.asList("application/json,text/html", "application/*,*/*"), Type.parser);
        assertEquals(4, resolved.size());
        assertEquals("application/json", resolved.get(0).toString());
        assertEquals("text/html", resolved.get(1).toString());
        assertEquals("application/*", resolved.get(2).toString());
        assertEquals("*/*", resolved.get(3).toString());
    }

    @Test
    public void testSortWithWeights() {
        final List<Type> resolved = WeightedResolver.resolve(Arrays.asList("application/json,application/*;q=0.8", "*/*;q=0.7,text/html"), Type.parser);
        assertEquals(4, resolved.size());
        assertEquals("application/json", resolved.get(0).toString());
        assertEquals("text/html", resolved.get(1).toString());
        assertEquals("application/*", resolved.get(2).toString());
        assertEquals("*/*", resolved.get(3).toString());
    }

}
