package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyUtils;
import dev.httpmarco.polocloud.node.dependencies.common.AbstractDependency;
import dev.httpmarco.polocloud.node.dependencies.xml.DependencyScheme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@ToString
public final class DependencyImpl extends AbstractDependency implements Dependency {

    private final String checksum;
    private final String repository;
    private final Collection<Dependency> depend;

    public DependencyImpl(String groupId, String artifactId, String version, String repository) {
        this.groupId(groupId).artifactId(artifactId).version(version);
        this.repository = repository;
        this.depend = new LinkedList<>();
        this.checksum = DependencyUtils.readChecksum(this);
    }

    public DependencyImpl(@NotNull DependencyScheme scheme, String repository) {
        this(scheme.groupId(), scheme.artifactId(), scheme.version(), repository);
    }

    @Override
    public boolean available() {
        return super.available() && checksum != null;
    }

    @Override
    public @NotNull String url() {
        return String.format(this.repository, urlGroupId(), artifactId(), version(), artifactId(), version());
    }

    @Contract(pure = true)
    @Override
    public @NotNull String fileName() {
        return String.format("%s-%s.jar", artifactId(), version());
    }
}