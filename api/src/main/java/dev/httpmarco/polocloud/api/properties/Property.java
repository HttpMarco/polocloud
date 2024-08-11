package dev.httpmarco.polocloud.api.properties;

import dev.httpmarco.polocloud.api.Named;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public final class Property<T> implements Named {

    private final String name;
    private final String classType;

    @Contract(pure = true)
    public Property(String name, @NotNull Class<?> classType) {
        this.name = name;
        this.classType = classType.getName();
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public @NotNull Class<T> clazz() {
        return (Class<T>) Class.forName(classType);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Property<Integer> ofInteger(String id) {
        return new Property<>(id, Integer.class);
    }
}