package io.nonstop.core.util.data;


class ChildDataNode extends BaseDataNode {

    private final Path.Segment segment;

    private final BaseDataNode parent;

    ChildDataNode(final Path.Segment segment, final BaseDataNode parent) {
        this.segment = segment;
        this.parent = parent;
    }

    @Override
    public <T> T as(final Class<T> type, final T defaultValue) {
        return retrieveValue(new Path(), type, defaultValue);
    }

    @Override
    public void set(final Object value) {
        storeValue(new Path(), value);
    }

    @Override
    <T> T retrieveValue(final Path path, final Class<T> type, final T defaultValue) {
        path.shift(segment);
        return parent.retrieveValue(path, type, defaultValue);
    }

    @Override
    void storeValue(final Path path, final Object value) {
        path.shift(segment);
        parent.storeValue(path, value);
    }
}
