package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collection;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class DependencyImpl implements Dependency {

    private String groupId;
    private String artifactId;
    private String version;
    private String checksum;
    private String repository;
    private Collection<Dependency> depend;

    @Override
    public boolean available() {
        return groupId != null && artifactId != null && version != null;
    }
}
