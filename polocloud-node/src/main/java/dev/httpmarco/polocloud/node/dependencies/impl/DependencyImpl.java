package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class DependencyImpl implements Dependency {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String checksum;
    private final String repository;
    private final Collection<Dependency> depend;

    public DependencyImpl(String groupId, String artifactId, String version, String repository) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repository = repository;
        this.depend = new LinkedList<>();
        this.checksum = DependencyUtils.readChecksum(this);
    }

    @Override
    public boolean available() {
        return groupId != null && artifactId != null && version != null;
    }

    @Override
    public @NotNull String url() {
        return String.format(this.repository, urlGroupId(), artifactId, version, artifactId, version);
    }

    @Contract(pure = true)
    private @NotNull String urlGroupId() {
        return groupId.replace('.', '/');
    }
}