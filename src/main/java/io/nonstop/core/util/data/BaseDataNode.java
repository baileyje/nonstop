package io.nonstop.core.util.data;


import java.util.*;

public abstract class BaseDataNode implements DataNode {

    abstract <T> T retrieveValue(final Path path, final Class<T> type, final T defaultValue);

    abstract void storeValue(final Path path, final Object value);

    @Override
    public boolean isNull() {
        return asObject() == null;
    }

    @Override
    public DataNode get(final String key) {
        return new ChildDataNode(new Path.KeySegment(key), this);
    }

    @Override
    public DataNode get(final int index) {
        return new ChildDataNode(new Path.IndexSegment(index), this);
    }

    @Override
    public <T> T as(Class<T> type) {
        return as(type, null);
    }

    @Override
    public <T> T as(Class<T> type, T defaultValue) {
        return defaultValue;
    }

    @Override
    public Object asObject() {
        return as(Object.class);
    }

    @Override
    public String asString() {
        return as(String.class);
    }

    @Override
    public String asStringValue() {
        return as(String.class, "");
    }

    @Override
    public Integer asInteger() {
        return as(Integer.class);
    }

    @Override
    public int asIntValue() {
        return as(Integer.class, -1);
    }

    @Override
    public Double asDouble() {
        return as(Double.class);
    }

    @Override
    public double asDoubleValue() {
        return as(Double.class, -1.0);
    }

    @Override
    public Boolean asBoolean() {
        return as(Boolean.class);
    }

    @Override
    public boolean asBooleanValue() {
        return as(Boolean.class, false);
    }

    @Override
    public void set(final Object value) {
    }

    @Override
    public void set(final String key, final Object value) {
        final Path path = new Path();
        path.shift(key);
        storeValue(path, value);
    }

    @Override
    public Iterator<DataNode> iterator() {
        final List listValue = as(List.class, null);
        if (listValue != null) {
            final List<DataNode> childNodes = new ArrayList<>(listValue.size());
            for (int index = 0; index < listValue.size(); index++) {
                childNodes.add(new ChildDataNode(new Path.IndexSegment(index), this));
            }
            return childNodes.iterator();
        }
        final Map mapValue = as(Map.class, null);
        if (mapValue != null) {
            final List<DataNode> childNodes = new ArrayList<>(mapValue.size());
            for (Object key : mapValue.keySet()) {
                childNodes.add(new ChildDataNode(new Path.KeySegment(key), this));
            }
            return childNodes.iterator();
        }
        final Object value = asObject();
        if (value != null) {
            return Collections.<DataNode>singletonList(new ValueDataNode(value)).iterator();
        }
        return Collections.emptyIterator();
    }

    static <T> T valueAs(final Object value, final Class<T> type, final T defaultValue) {
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return type.cast(value);
        }
        return defaultValue;
    }
}
