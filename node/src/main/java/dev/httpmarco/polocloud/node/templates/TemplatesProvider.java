package dev.httpmarco.polocloud.node.templates;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

public final class TemplatesProvider {

    private static final Path TEMPLATE_DIR = Path.of("templates");

    static {
        TEMPLATE_DIR.toFile().mkdirs();
    }

    @SneakyThrows
    public void prepareTemplate(String templateId) {
        var templatePath = TEMPLATE_DIR.resolve(templateId);

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
