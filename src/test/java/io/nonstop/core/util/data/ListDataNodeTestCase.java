package io.nonstop.core.util.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ListDataNodeTestCase {

    private List<Object> data = new ArrayList<>();

    private ValueDataNode node = new ValueDataNode(data);

    @Test
    public void testGetValidDirect() {
        data.add("String Value");
        final DataNode res = node.get(0);
        assertNotNull(res);
        assertEquals("String Value", res.asString());
        assertFalse(res.isNull());
    }

    @Test
    public void testGetMissingDirect() {
        final DataNode res = node.get(0);
        assertNull(res.asString());
    }

    @Test
    public void testInvalidType() {
        data.add("String Value");
        assertNull(node.get(0).asInteger());
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
        data.add(new HashMap<String, Object>() {{
            put("second", "String Value");
        }});
        final DataNode res = node.get(0).get("second");
        assertNotNull(res);
        assertEquals("String Value", res.asString());
    }

    @Test
    public void testGetInvalidNested() {
        final DataNode res = node.get("first").get(0);
        assertNull(res.asString());
    }

    @Test
    public void testSetDirectNoOp() {
        node.set("Some value");
        assertTrue(data.isEmpty());
    }

    @Test
    public void testSetNestedExists() {
        final Map nested = new HashMap<>();
        data.add(nested);
        node.get(0).set("second", "some value");
        assertEquals("some value", nested.get("second"));
        assertEquals("some value", node.get(0).get("second").asString());
    }

    @Test
    public void testSetNestedNotExists() {
        node.get(0).set("second", "some value");
        assertEquals("some value", node.get(0).get("second").asString());
    }

    @Test
    public void testSetNestedInvalid() {
        data.add("not a map");
        node.get(0).set("second", "some value");
        assertNull(node.get(0).get("second").asString());
    }
}
