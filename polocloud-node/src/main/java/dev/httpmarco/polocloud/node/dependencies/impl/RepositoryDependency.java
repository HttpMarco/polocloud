package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@Getter
@ToString
@Accessors(fluent = true)
@AllArgsConstructor
public final class RepositoryDependency implements Dependency {

    private final String groupId;
    private final String artifactId;
    private final Version version;

    private @Nullable File file;

    @Override
    public boolean available() {
        return file != null && file.exists();
    }
}
