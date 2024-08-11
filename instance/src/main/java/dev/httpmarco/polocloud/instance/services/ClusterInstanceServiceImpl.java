package dev.httpmarco.polocloud.instance.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class ClusterInstanceServiceImpl implements ClusterService {

    private final UUID id;
    private final int orderedId;
    private final int port;
    private final String hostname;
    private final String runningNode;
    private final ClusterServiceState state;
    private ClusterGroup group;

    @Override
    public void shutdown() {
        //todo
    }

    @Override
    public void executeCommand(String command) {
        //todo
    }

    @Override
    public void update() {
        //todo
    }

    @Override
    public String details() {
        return this.toString();
    }
}
