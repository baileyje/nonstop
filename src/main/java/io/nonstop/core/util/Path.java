package io.nonstop.core.util;

import io.undertow.util.MimeMappings;

public class Path {

    private final String path;

    public Path(String path) {
        this.path = path;
    }

    public String extension() {
        int index = path.lastIndexOf('.');
        if (index != -1 && index != path.length() - 1) {
            return path.substring(index + 1);
        }
        return null;
    }

    public String mimetype() {
        final String ext = extension();
        if (ext != null) {
            return MimeMappings.DEFAULT.getMimeType(ext);
        }
        return null;
    }

    public static String mimetype(final String path) {
        return new Path(path).mimetype();
    }
}
