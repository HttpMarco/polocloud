package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class ClusterLocalServiceImpl extends ClusterServiceImpl {

    private final Process process;
    private final Path path;

    public ClusterLocalServiceImpl(int id, UUID uniqueId, ClusterGroup group, Process process, Path path) {
        super(id, uniqueId, group);

        this.process = process;
        this.path = path;
    }
}
