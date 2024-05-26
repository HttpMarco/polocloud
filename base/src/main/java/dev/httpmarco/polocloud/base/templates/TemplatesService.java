package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.osgan.files.json.JsonDocument;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.common.PropertiesPoolSerializer;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.groups.CloudServiceGroupProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class TemplatesService {

    public static final Path TEMPLATES = Path.of("templates");

    @Getter(AccessLevel.PACKAGE)
    private final JsonDocument<TemplatesConfig> document;

    public TemplatesService() {
        Files.createDirectoryIfNotExists(TEMPLATES);

        this.document = new JsonDocument<>(new TemplatesConfig(), TEMPLATES.resolve("templates.json"), new PropertiesPoolSerializer());

        this.createTemplates("every");
        this.createTemplates("every_server");
        this.createTemplates("every_proxy");
    }

    public boolean createTemplates(String name, String... mergedTemplates) {
        if (isTemplate(name)) {
            return false;
        }

        var directory = TEMPLATES.resolve(name);
        var template = new Template(name, mergedTemplates);
        Files.createDirectoryIfNotExists(directory);
        templates().add(template);
        this.document.updateDocument();

        return true;
    }

    public boolean isTemplate(String id) {
        return templates().stream().anyMatch(it -> it.id().equalsIgnoreCase(id));
    }

    public List<Template> templates() {
        return this.document.value().templates();
    }

    public Template templates(String name) {
        return templates().stream().filter(it -> it.id().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
