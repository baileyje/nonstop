package io.nonstop.core.middleware;

import org.junit.Test;


import java.util.Collections;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Test chain step.
 *
 * @author John Bailey
 */
public class StepTestCase {

    @Test
    public void testSlash() {
        final Step step = new Step("/",  false,null);
        final Step.MatchResult result = step.matches("/some/path");
        assertNotNull(result);
        assertEquals("/some/path", result.remainingPath);
        assertEquals(result.params, Collections.emptyMap());
    }

    @Test
    public void testStrictMatch() {
        final Step step = new Step("/foo", true,null);
        final Step.MatchResult result = step.matches("/foo");
        assertNotNull(result);
        assertEquals("", result.remainingPath);
        assertEquals(result.params, Collections.emptyMap());
    }

    @Test
    public void testStrictNoMatch() {
        final Step step = new Step("/foo", true,null);
        final Step.MatchResult result = step.matches("/bar");
        assertNull(result);
    }

    @Test
    public void testStrictPartialNoMatch() {
        final Step step = new Step("/foo", true,null);
        final Step.MatchResult result = step.matches("/foo/bar");
        assertNull(result);
    }

    @Test
    public void testNonStrictNoParamsPartialMatch() {
        final Step step = new Step("/foo", false,null);
        final Step.MatchResult result = step.matches("/foo/bar");
        assertNotNull(result);
        assertEquals("/bar", result.remainingPath);
        assertEquals(result.params, Collections.emptyMap());
    }

    @Test
    public void testNonStrictEndParam() {
        final Step step = new Step("/foo/:bar", false, null);
        final Step.MatchResult result = step.matches("/foo/bar");
        assertNotNull(result);
        assertEquals("", result.remainingPath);
        assertEquals(result.params, new HashMap<String, String>() {{ this.put("bar", "bar"); }});
    }

    @Test
    public void testNonStrictMiddleParam() {
        final Step step = new Step("/foo/:bar", false,null);
        final Step.MatchResult result = step.matches("/foo/bar/baz");
        assertNotNull(result);
        assertEquals("/baz", result.remainingPath);
        assertEquals(result.params, new HashMap<String, String>() {{ this.put("bar", "bar"); }});
    }

    @Test
    public void testNonStrictMultiParamsEnd() {
        final Step step = new Step("/foo/:bar/:baz", false,null);
        final Step.MatchResult result = step.matches("/foo/bar/baz");
        assertNotNull(result);
        assertEquals("", result.remainingPath);
        assertEquals(result.params, new HashMap<String, String>() {{
            this.put("bar", "bar");
            this.put("baz", "baz");
        }});
    }

    @Test
    public void testNonStrictMultiParamsMiddle() {
        final Step step = new Step("/foo/:bar/:baz", false,null);
        final Step.MatchResult result = step.matches("/foo/bar/baz/qak");
        assertNotNull(result);
        assertEquals("/qak", result.remainingPath);
        assertEquals(result.params, new HashMap<String, String>() {{
            this.put("bar", "bar");
            this.put("baz", "baz");
        }});
    }

}
