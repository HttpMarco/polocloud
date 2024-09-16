package dev.httpmarco.polocloud.api.properties;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
public final class PropertiesPool {

    private final Map<Property<?>, Object> pool = new HashMap<>();

    public boolean has(Property<?> property) {
        return this.pool.containsKey(property);
    }

    public <T> void put(Property<T> property, T value) {
        this.pool.put(property, value);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T property(Property<T> property) {
        return (T) this.pool.get(property);
    }

    public void extendsProperties(@NotNull PropertiesPool pool) {
        this.pool.putAll(pool.pool);
    }
}
