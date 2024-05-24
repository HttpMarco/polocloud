package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Files;

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

    @SneakyThrows
    public void copy(LocalCloudService localCloudService) {

        if (canUsed) {
            Files.copy(TemplatesService.TEMPLATES.resolve(id), localCloudService.runningFolder());
        }

        for (var templateName : this.mergedTemplates) {
            var template = CloudBase.instance().templatesService().templates(templateName);

            if (template != null) {
                template.copy(localCloudService);
            }
        }
    }
}
