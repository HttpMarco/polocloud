package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.NodeConfig;
import dev.httpmarco.polocloud.node.cluster.impl.LocalNodeImpl;
import dev.httpmarco.polocloud.node.cluster.tasks.HeadNodeDetection;
import dev.httpmarco.polocloud.node.packets.ClusterAuthTokenPacket;
import dev.httpmarco.polocloud.node.packets.ClusterMergeFamilyPacket;
import dev.httpmarco.polocloud.node.packets.ClusterReloadCallPacket;
import dev.httpmarco.polocloud.node.packets.ClusterRequireReloadPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Log4j2
@Getter
@Accessors(fluent = true)
public final class ClusterProviderImpl implements ClusterProvider {

    private final LocalNode localNode;
    private NodeEndpoint headNode;
    private final Set<NodeEndpoint> endpoints;

    public ClusterProviderImpl(@NotNull NodeConfig config) {
        this.localNode = new LocalNodeImpl(config.localNode());
        this.endpoints = new HashSet<>();

        localNode.transmit().responder("auth-cluster-token", property -> {
            boolean value = config.clusterToken().equals(property.getString("token"));
            // todo close connection here and check all incoming packets !!! important
            log.warn("External try to authenticate with the cluster token&8. &7The result is &b{}&8.", value);
            return new ClusterAuthTokenPacket(value);
        });

        localNode.transmit().listen(ClusterMergeFamilyPacket.class, (transmit, packet) -> {
            // todo security issue
            config.clusterId(packet.clusterId());
            config.clusterToken(packet.clusterToken());
            config.nodes().clear();
            config.nodes().addAll(packet.allClusterEndpoints());

            Node.instance().updateNodeConfig();
            log.info("The cluster has been merged with the family&8. The cluster id is now &b{}&8.", packet.clusterId());

            //todo connect with all other new endpoints
        });
        localNode.transmit().listen(ClusterRequireReloadPacket.class, (transmit, packet) -> broadcastAll(new ClusterReloadCallPacket()));
        localNode.transmit().listen(ClusterReloadCallPacket.class, (transmit, packet) -> {

            // reloading first all groups
            Node.instance().groupProvider().reload();
        });
    }

    @Override
    public boolean localHead() {
        return this.headNode.equals(localNode);
    }

    @Override
    public NodeEndpoint find(String nodeId) {
        return localNode.data().name().equalsIgnoreCase(nodeId) ? localNode : endpoints.stream().filter(it -> it.data().name().equalsIgnoreCase(nodeId)).findFirst().orElse(null);
    }

    @Override
    public void broadcast(Packet packet) {
        for (var endpoint : this.endpoints) {
            Objects.requireNonNull(endpoint.transmit()).sendPacket(packet);
        }
    }

    @Override
    public void broadcastAll(Packet packet) {
        Objects.requireNonNull(this.localNode.transmit()).sendPacket(packet);
        for (var endpoint : this.endpoints) {
            Objects.requireNonNull(endpoint.transmit()).sendPacket(packet);
        }
    }


    public void initialize() {
        // detect head node
        this.headNode = HeadNodeDetection.detect(this);
        log.info("The cluster use {} as the head node&8.", this.headNode.data().name());

        if (!headNode.equals(localNode)) {
            // todo sync
        } else {
            localNode.initialize();
            localNode.situation(NodeSituation.RECHEABLE);
        }
    }

    @Override
    public void close() {
        this.localNode.close();
    }
}
