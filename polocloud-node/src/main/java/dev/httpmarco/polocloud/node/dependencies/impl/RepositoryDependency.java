package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@ToString
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class RepositoryDependency implements Dependency {

    private final String groupId;
    private final String artifactId;
    private final Version version;

    private final String repository = "https://repo1.maven.org/maven2/";
    private @Nullable File file;

    @Override
    public boolean available() {
        return file != null && file.exists();
    }

    public void download() {
        var downloadTarget = Path.of("dependencies").resolve(artifactId + "-" + version + ".jar");

        if (Files.exists(downloadTarget)) {
            this.file = downloadTarget.toFile();
            return;
        }

        var downloadUrl = this.repository + "/" + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/" + downloadTarget.getFileName().getFileName() + ".jar";
        // todo download file
    }
}
