package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class Template {

    private final String id;
    private final boolean canUsed;
    private final String[] mergedTemplates;
    private final PropertiesPool properties;

    public Template(String id, String... mergedTemplates) {
        this.id = id;
        this.canUsed = true;
        this.mergedTemplates = mergedTemplates;
        this.properties = new PropertiesPool();
    }
}
