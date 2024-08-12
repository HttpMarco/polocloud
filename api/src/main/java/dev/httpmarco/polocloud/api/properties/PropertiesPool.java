package dev.httpmarco.polocloud.api.properties;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public final class PropertiesPool {

    private final Map<Property<?>, Object> properties = new HashMap<>();

    public boolean has(Property<?> property) {
        return this.properties.containsKey(property);
    }


    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T property(Property<T> property) {
        return (T) this.properties.get(property);
    }
}
