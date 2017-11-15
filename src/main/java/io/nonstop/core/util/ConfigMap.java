package io.nonstop.core.util;

import java.util.HashMap;
import java.util.Map;

public class ConfigMap {

    private final Map<String, Object> config = new HashMap<>();

    public ConfigMap get(final String key) {
        ConfigMap child = this.get(key, ConfigMap.class);
        if (child == null) {
            child = new ConfigMap();
            config.put(key, child);
        }
        return child;
    }

    public <T> T get(final String key, final Class<T> type) {
        final Object value = config.get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return type.cast(value);
        }
        return null;
    }

    public void put(final String key, final Object value) {
        config.put(key, value);
    }

}
