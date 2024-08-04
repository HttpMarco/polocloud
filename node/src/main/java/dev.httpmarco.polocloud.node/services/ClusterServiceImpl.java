package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ClusterServiceImpl implements ClusterService {

    private final ClusterGroup group;
    private final int orderedId;
    private final UUID id;
    private final int port;
    private final String hostname;
    private final String runningNode;

    @Setter
    private ClusterServiceState state = ClusterServiceState.PREPARED;

    @Override
    public String details() {
        return "id&8=&7" + id + "&8, &7hostname&8=&7" + hostname + ", &7port&8=&7" + port + "&8, &7node&8=&7" + runningNode;
    }

    @Override
    public void shutdown() {
        // todo find node and send him shutdown call
    }

    @Override
    public void executeCommand(String command) {
        // todo bridge
    }
}
