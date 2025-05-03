package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

    @Setter(AccessLevel.PROTECTED)
    private ClusterServiceState state;

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
