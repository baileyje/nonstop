package io.nonstop.core.util.data;


import java.util.*;


public class Path {

    private final LinkedList<Segment> segments = new LinkedList<>();

    public void shift(final Segment segment) {
        segments.addFirst(segment);
    }

    public void shift(final String key) {
        segments.addFirst(new KeySegment(key));
    }

    public void shift(final int index) {
        segments.addFirst(new IndexSegment(index));
    }

    @Override
    public String toString() {
        return segments.toString();
    }

    private Iterable<Segment> containerPath() {
        if (segments.size() > 0) {
            return segments.subList(0, segments.size() - 1);
        }
        return Collections.emptyList();
    }

    <T> T value(final Object from, final Class<T> asType, final T defaultValue) {
        final Path.Segment valueSegment = segments.getLast();
        Object current = from;
        for (Path.Segment segment : containerPath()) {
            current = segment.value(current);
        }
        return valueSegment.value(current, asType, defaultValue);
    }

    void store(final Object value, final Object into) {
        Segment last = null;
        Object target = into;
        for (Path.Segment segment : segments) {
            Object container;
            if (last != null) {
                container = segment.container(last.value(target));
                last.set(container, target);
            } else {
                container = segment.container(target);
            }
            if (container == null) {
                return; // We can't continue as the path segment doesn't match data node type,
            }
            target = container;
            last = segment;
        }
        if (last != null) {
            last.set(value, target);
        }
    }



    static abstract class Segment {

        abstract Object value(final Object from);

        abstract Object container(final Object from);

        <T> T value(final Object from, final Class<T> asType, final T defaultValue) {
            final Object value = value(from);
            if (value != null && asType.isAssignableFrom(value.getClass())) {
                return asType.cast(value);
            }
            return defaultValue;
        }

        abstract void set(final Object value, final Object into);
    }

    static class KeySegment extends Segment {
        private final Object key;

        KeySegment(final Object key) {
            this.key = key;
        }

        @Override
        public Object value(final Object from) {
            final Map map = BaseDataNode.valueAs(from, Map.class, Collections.emptyMap());
            return map.get(key);
        }

        @Override
        Object container(final Object from) {
            if (from == null) {
                return new HashMap<>();
            } else if (from  instanceof Map) {
                return from;
            }
            return null;
        }

        @Override
        void set(final Object value, final Object into) {
            final Map map = BaseDataNode.valueAs(into, Map.class, Collections.emptyMap());
            map.put(key, value);
        }

        @Override
        public String toString() {
            return "[" + key  + "]";
        }
    }

    static class IndexSegment extends Segment {

        private final int index;

        IndexSegment(final int index) {
            this.index = index;
        }

        @Override
        public Object value(final Object from) {
            final List list = BaseDataNode.valueAs(from, List.class, Collections.emptyList());
            if (list.size() > index) {
                return list.get(index);
            }
            return null;
        }

        @Override
        Object container(final Object from) {
            if (from == null) {
                return new ArrayList<>();
            } else if (from  instanceof List) {
                return from;
            }
            return null;
        }

        @Override
        void set(final Object value, final Object into) {
            final List list = BaseDataNode.valueAs(into, List.class, Collections.emptyList());
            list.add(index, value);
        }

        @Override
        public String toString() {
            return "[" + index  + "]";
        }
    }

}
