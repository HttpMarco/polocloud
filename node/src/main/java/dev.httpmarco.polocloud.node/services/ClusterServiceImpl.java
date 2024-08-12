package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceCommandPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceLogPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceShutdownCallPacket;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    @SneakyThrows
    public List<String> logs() {
        var future = new CompletableFuture<List<String>>();
        node().transmit().request("service-log", new CommunicationProperty().set("id", id), ServiceLogPacket.class, serviceLogPacket -> future.complete(serviceLogPacket.logs()));
        return future.get(5, TimeUnit.SECONDS);
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
        node().transmit().sendPacket(new ServiceCommandPacket(this.id, command));
    }

    @Override
    public void update() {
        // todo call head node and broadcast this to all nodes
    }

    public NodeEndpoint node() {
        return Node.instance().clusterProvider().find(runningNode);
    }
}
