package io.nonstop.core.util.data;


public class ValueDataNode extends BaseDataNode {

    private Object value;

    public ValueDataNode(final Object value) {
        this.value = value;
    }

    @Override
    public <T> T as(final Class<T> type, final T defaultValue) {
        return valueAs(value, type, defaultValue);
    }

    @Override
    public void set(final Object value) {
        this.value = value;
    }

    @Override
    public void set(String key, Object value) {
        final Path path = new Path();
        path.shift(key);
        storeValue(path, value);
    }

    @Override
    <T> T retrieveValue(final Path path, final Class<T> type, final T defaultValue) {
        return path.value(value, type, defaultValue);
    }

    @Override
    void storeValue(final Path path, final Object value) {
        path.store(value, this.value);
    }
}
