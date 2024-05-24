package dev.httpmarco.polocloud.api.properties;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Property<T> {

    private final String id;
    private final Class<T> type;

    public Property(String id, Class<T> type) {
        this.id = id;
        this.type = type;

        PropertiesPool.PROPERTY_LIST.add(this);
    }
}
