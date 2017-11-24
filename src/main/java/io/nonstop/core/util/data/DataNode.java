package io.nonstop.core.util.data;


import java.util.Iterator;

public interface DataNode extends Iterable<DataNode> {

    DataNode get(final String key);

    DataNode get(final int index);

    boolean isNull();

    <T> T as(Class<T> type);

    <T> T as(Class<T> type, T defaultValue);

    Object asObject();

    String asString();

    String asStringValue();

    Integer asInteger();

    int asIntValue();

    Double asDouble();

    double asDoubleValue();

    Boolean asBoolean();

    boolean asBooleanValue();

    void set(final Object value);

    void set(final String key, final Object value);

    Iterator<DataNode> iterator();
}

