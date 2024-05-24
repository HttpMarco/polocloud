package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.polocloud.api.properties.Property;

public final class TemplateProperties<T> extends Property<T> {

    public TemplateProperties(String id, Class<T> type) {
        super(id, type);
    }
}
