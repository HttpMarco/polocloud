package dev.httpmarco.polocloud.api.properties;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
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
        return (Class<T>) CloudAPI.instance().classByName(classType);
    }

    public static <T> Property<T> of(String id, Class<T> clazz) {
        return new Property<>(id, clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Property<?> property && property.name.equalsIgnoreCase(this.name);
    }
}