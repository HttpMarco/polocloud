package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.NodeConfig;
import dev.httpmarco.polocloud.node.cluster.impl.ExternalNode;
import dev.httpmarco.polocloud.node.cluster.impl.LocalNodeImpl;
import dev.httpmarco.polocloud.node.cluster.tasks.HeadNodeDetection;
import dev.httpmarco.polocloud.node.packets.ClusterAuthTokenPacket;
import dev.httpmarco.polocloud.node.packets.ClusterMergeFamilyPacket;
import dev.httpmarco.polocloud.node.packets.ClusterReloadCallPacket;
import dev.httpmarco.polocloud.node.packets.ClusterRequireReloadPacket;
import dev.httpmarco.polocloud.node.packets.node.NodeHeadRequestPacket;
import dev.httpmarco.polocloud.node.packets.node.NodeSituationResponsePacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

        localNode.transmit().responder("node-state", property -> new NodeSituationResponsePacket(localNode.situation()));

        localNode.transmit().responder("auth-cluster-token", property -> {
            boolean value = config.clusterToken().equals(property.getString("token"));
            // todo close connection here and check all incoming packets !!! important
            log.warn("External try to authenticate with the cluster token&8. &7The result is &b{}&8.", value);
            return new ClusterAuthTokenPacket(value);
        });

        localNode.transmit().responder("node-head-request", property -> new NodeHeadRequestPacket(headNode.data().name()));

        localNode.transmit().listen(ClusterMergeFamilyPacket.class, (transmit, packet) -> {
            // todo security issue
            config.clusterId(packet.clusterId());
            config.clusterToken(packet.clusterToken());
            config.nodes().clear();
            config.nodes().addAll(packet.allClusterEndpoints());

            // append connection to all other nodes
            endpoints.addAll(packet.allClusterEndpoints().stream().map(ExternalNode::new).toList());
            Node.instance().updateNodeConfig();

            //todo sync all this things

            log.info("The cluster has been merged with the family&8. &7The cluster id is now &b{}&8.", packet.clusterId());
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
            if(endpoint.situation() == NodeSituation.RECHEABLE) { // todo maybe initialize queue packets
                Objects.requireNonNull(endpoint.transmit()).sendPacket(packet);
            }
        }
    }

    @Override
    public void broadcastAll(Packet packet) {
        Objects.requireNonNull(this.localNode.transmit()).sendPacket(packet);
        this.broadcast(packet);
    }


    public void initialize() {
        for (var data : Node.instance().nodeConfig().nodes()) {
            var externalNode = new ExternalNode(data);
            var future = new CompletableFuture<>();

            externalNode.connect(transmit -> {
                transmit.request("node-state", NodeSituationResponsePacket.class, (it) -> {
                    externalNode.situation(it.situation());
                    future.complete(true);
                });
            }, transmit -> {
                externalNode.situation(NodeSituation.STOPPED);
                future.complete(false);
            });
            this.endpoints.add(externalNode);
            future.join();
        }
        this.headNode = HeadNodeDetection.detect(this);
        // detect head node
        log.info("The cluster use &b{} &7as the head node&8.", localHead() ? "his self&7" : this.headNode.data().name());

        if (!localHead()) {
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
