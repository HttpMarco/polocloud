package dev.httpmarco.polocloud.suite.templates;

import dev.httpmarco.polocloud.suite.services.ClusterLocalService;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public final class Template {

    private final String name;
    private final Path path;

    public Template(String name) {
        this.name = name;
        this.path = Path.of("local").resolve("templates").resolve(name);
    }

    public void bind(@NotNull ClusterLocalService localService) {
        PathUtils.copyDirectoryContents(path.toString(), localService.path().toString());
    }
}