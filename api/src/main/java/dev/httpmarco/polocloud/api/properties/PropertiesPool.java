package dev.httpmarco.polocloud.api.properties;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Getter
@Accessors(fluent = true)
public final class PropertiesPool<T extends Property<?>> {

    private final HashMap<T, Object> properties = new HashMap<>();

    public boolean has(T key) {
        return properties.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <R, P extends Property<R>> void put(P property, R value) {
        this.properties.put((T) property, value);
    }

    public void remove(T property) {
        this.properties.remove(property);
    }

    @SuppressWarnings("unchecked")
    public <P> P property(Property<P> property) {
        return (P) this.properties.get(property);
    }

    public Object property(String key) {
        return this.properties.get(key);
    }
}
