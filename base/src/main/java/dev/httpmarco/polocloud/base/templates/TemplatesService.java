package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.osgan.files.Files;

import java.nio.file.Path;

public final class TemplatesService {

    private static final Path TEMPLATES = Path.of("templates");

    public TemplatesService() {
        Files.createDirectoryIfNotExists(TEMPLATES);
    }
}
