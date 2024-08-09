package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceShutdownCallPacket;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
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
        var node = node();

        if (node == null) {
            return;
        }

        node.transmit().sendPacket(new ServiceShutdownCallPacket(id));
    }

    @Override
    public void executeCommand(String command) {
        // todo bridge
    }

    @Override
    public void update() {
        // todo call head node and broadcast this to all nodes
    }

    public NodeEndpoint node() {
        return Node.instance().clusterProvider().find(runningNode);
    }
}
