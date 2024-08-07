package dev.httpmarco.polocloud.base.node.tasks;

import dev.httpmarco.osgan.networking.CommunicationFuture;
import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.node.NodeHeadCalculator;
import dev.httpmarco.polocloud.base.node.NodeProvider;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.packets.ClusterEndpointStatePacket;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class ClusterBindTask {

    @Contract("_ -> new")
    public @NotNull CompletableFuture<Void> bindCluster(@NotNull NodeProvider headProvider) {
        return CompletableFuture.allOf(headProvider.externalNodeEndpoints().stream().map(ClusterBindTask::bind).toArray(CompletableFuture[]::new));
    }

    public @NotNull CommunicationFuture<Void> bind(@NotNull ExternalNodeEndpoint nodeEndpoint) {
        var client = new CommunicationClient(nodeEndpoint.data().hostname(), nodeEndpoint.data().port());
        var future = new CommunicationFuture<Void>();

        client.clientAction(CommunicationClientAction.CONNECTED, channel -> {
            // First we must detect the head node of the current cluster
            nodeEndpoint.situation(NodeSituation.DETECT_SIT);
            nodeEndpoint.transmit(channel);

            nodeEndpoint.transmit().request("cluster-node-endpoint-state", ClusterEndpointStatePacket.class, packet -> {
                nodeEndpoint.situation(packet.nodeSituation());
                nodeEndpoint.onlineDuration(packet.onlineDuration());
                future.complete(null);
            });
        }).clientAction(CommunicationClientAction.FAILED, it -> {
            Node.instance().logger().info("Cluster cannot bind " + nodeEndpoint.data().id() + ". Because endpoint is offline!");
            nodeEndpoint.situation(NodeSituation.NOT_AVAILABLE);
            future.complete(null);
        }).clientAction(CommunicationClientAction.DISCONNECTED, channelTransmit -> {
            // get the specific node
            ExternalNodeEndpoint node = Node.instance().nodeProvider().node(channelTransmit);

            Node.instance().logger().info("The " + node.data().id() + " is now offline... ");
            node.situation(NodeSituation.NOT_AVAILABLE);

            if (Node.instance().nodeProvider().headNodeEndpoint().equals(node)) {
                var newHead = NodeHeadCalculator.calculateNewHead();
                Node.instance().nodeProvider().headNodeEndpoint(newHead);
                Node.instance().logger().warn("This node was the head node of the cluster... calculate a new head node for balance: " + newHead.data().id());
            }
        });
        client.initialize();
        return future;
    }
}