package dev.httpmarco.polocloud.node.properties;


import dev.httpmarco.polocloud.api.properties.Property;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@UtilityClass
public final class PropertyRegister {

    private static final List<Property<?>> properties = new ArrayList<>();

    public void register(Property<?> property) {
        properties.add(property);
        log.debug("Register a new node property: {}", property.name());
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
