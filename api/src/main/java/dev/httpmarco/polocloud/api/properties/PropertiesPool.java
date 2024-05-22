package dev.httpmarco.polocloud.api.properties;

import java.util.HashMap;

public final class PropertiesPool {

    private final HashMap<String, Object> properties = new HashMap<>();

    public boolean has(String key) {
        return properties.containsKey(key);
    }

    public void put(String key, Object value) {
        this.properties.put(key, value);
    }

    public void remove(String key) {
       this.properties.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T property(String key, Class<T> clazz) {
       return (T) this.properties.get(key);
    }

    public Object property(String key) {
        return this.properties.get(key);
    }
}
