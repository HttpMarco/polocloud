package dev.httpmarco.polocloud.api.properties;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class PropertyRegister {

    private static final List<Property<?>> properties = new ArrayList<>();

    public void register(Property<?> property) {
        properties.add(property);
    }

    public Property<?> byName(String id) {
        return properties.stream().filter(it -> it.name().equals(id)).findFirst().orElse(null);
    }

    public int propertiesAmount() {
        return properties.size();
    }

    @Contract(pure = true)
    public void register(Property<?> @NotNull ... properties) {
        for (var property : properties) {
            register(property);
        }
    }
}
