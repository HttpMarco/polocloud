package dev.httpmarco.polocloud.api.properties;

import dev.httpmarco.polocloud.api.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class Property<T> implements Named {

    private String name;
    private String classType;

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public @NotNull Class<T> clazz() {
        return (Class<T>) Class.forName(classType);
    }
}
