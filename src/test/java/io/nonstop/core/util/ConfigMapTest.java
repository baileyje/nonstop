package io.nonstop.core.util;

import io.nonstop.core.util.ConfigMap;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class ConfigMapTest {

    private ConfigMap locals;

    @Before
    public void setUp() {
        locals = new ConfigMap();
    }

    @Test
    public void testGetMissing() {
        assertNull(locals.get("Not_Real"));
    }

    @Test
    public void testGetInvalidType() {
        locals.put("key", "String Value");
        assertNull(locals.get("key", Integer.class));
    }

    @Test
    public void testGetValidObject() {
        locals.put("key", "String Value");
        assertEquals("String Value", locals.get("key"));
    }

    @Test
    public void testGetValidTyped() {
        locals.put("key", "String Value");
        assertEquals("String Value", locals.get("key", String.class));
    }

}
