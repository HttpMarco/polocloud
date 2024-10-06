package dev.httpmarco.polocloud.node.templates;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class TemplatesProvider {

    private static final Path TEMPLATE_DIR = Path.of("templates");
    private final List<Template> templates = new ArrayList<>();

    static {
        TEMPLATE_DIR.toFile().mkdirs();
    }

    @SneakyThrows
    public void prepareTemplate(String templateId) {
        var templatePath = TEMPLATE_DIR.resolve(templateId);
        this.templates.add(new Template(templateId));

        if (!Files.exists(templatePath)) {
            Files.createDirectory(templatePath);
        }
    }

    public void prepareTemplate(String @NotNull [] templateIds) {
        for (var templateId : templateIds) {
            this.prepareTemplate(templateId);
        }
    }
}
