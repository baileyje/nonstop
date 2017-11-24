package io.nonstop.core.util.data;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapDataNodeTestCase {

    private Map<String, Object> data = new HashMap<>();
    private ValueDataNode node = new ValueDataNode(data);

    @Test
    public void testGetValidDirect() {
        data.put("key", "String Value");
        final DataNode res = node.get("key");
        assertNotNull(res);
        assertEquals("String Value", res.asString());
        assertFalse(res.isNull());
    }

    @Test
    public void testGetMissingDirect() {
        final DataNode res = node.get("missing");
        assertNull(res.asString());
    }

    @Test
    public void testInvalidType() {
        data.put("key", "String Value");
        final DataNode res = node.get("key");
        assertNull(res.asInteger());
    }

    @Test
    public void testMissingIntDefaultType() {
        assertEquals(-1, node.get("missing").asIntValue());
    }

    @Test
    public void testMissingDoubleDefaultType() {
        assertEquals(-1.0, node.get("missing").asDoubleValue());
    }

    @Test
    public void testMissingStringDefaultType() {
        assertEquals("", node.get("missing").asStringValue());
    }

    @Test
    public void testMissingDefaultType() {
        assertEquals("Some string", node.get("missing").as(String.class, "Some string"));
    }

    @Test
    public void testGetValidNested() {
        data.put("first", new HashMap<String, Object>() {{
            put("second", "String Value");
        }});
        final DataNode res = node.get("first").get("second");
        assertNotNull(res);
        assertEquals("String Value", res.asString());
    }

    @Test
    public void testGetInvalidNested() {
        final DataNode res = node.get("first").get("second");
        assertNull(res.asString());
    }

    @Test
    public void testSetDirectNoOp() {
        node.set("Some value");
        assertTrue(data.isEmpty());
    }

    @Test
    public void testSetDirectAtKey() {
        node.set("key", "Some value");
        assertEquals("Some value", data.get("key"));
        assertEquals("Some value", node.get("key").asString());
    }

    @Test
    public void testSetNestedExists() {
        final Map nested = new HashMap<>();
        data.put("first", nested);
        node.get("first").set("second", "some value");
        assertEquals("some value", nested.get("second"));
        assertEquals("some value", node.get("first").get("second").asString());
    }

    @Test
    public void testSetNestedNotExists() {
        node.get("first").set("second", "some value");
        assertEquals("some value", node.get("first").get("second").asString());
    }

    @Test
    public void testSetNestedInvalid() {
        data.put("first", "not a map");
        node.get("first").set("second", "some value");
        assertNull(node.get("first").get("second").asString());
    }
}
