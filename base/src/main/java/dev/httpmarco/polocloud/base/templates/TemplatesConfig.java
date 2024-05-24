package dev.httpmarco.polocloud.base.templates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class TemplatesConfig {

    private final List<Template> templates;

    public TemplatesConfig() {
        this.templates = new ArrayList<>();
    }
}
