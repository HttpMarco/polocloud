package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class ClusterServiceImpl implements ClusterService {

    private final int id;
    private final UUID uniqueId;
    private final ClusterGroup group;

    @Override
    public @NotNull String name() {
        return group.name() + "-" + id;
    }

    @Override
    public boolean executeCommand(String command) {
        //todo
        return false;
    }

    @Override
    public void shutdown() {
        // todo
    }
}
