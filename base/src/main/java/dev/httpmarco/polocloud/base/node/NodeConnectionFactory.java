package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.networking.CommunicationFuture;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.NodeEndpoint;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class NodeConnectionFactory {

    public static void bindCluster(@NotNull NodeProvider headProvider) {

        // start connection of the other nodes
        for (var nodeEndpoint : headProvider.externalNodeEndpoints()) {
            // we must wait of all connection states
            bind(nodeEndpoint).sync(null, 5);
        }

        // detect head node
        var headNode = headProvider.externalNodeEndpoints().stream().filter(it -> it.situation() == NodeSituation.REACHABLE).map(it -> (NodeEndpoint) it).findFirst().orElse(headProvider.localEndpoint());

        headProvider.headNodeEndpoint(headNode);
        Node.instance().logger().info("Switch to head node: " + headProvider.headNodeEndpoint().data().id());
    }

    public static @NotNull CommunicationFuture<Void> bind(@NotNull ExternalNodeEndpoint nodeEndpoint) {
        var client = new CommunicationClient(nodeEndpoint.data().hostname(), nodeEndpoint.data().port());
        var future = new CommunicationFuture<Void>();

        client.clientAction(CommunicationClientAction.CONNECTED, channel -> {
            // First we must detect the head node of the current cluster
            nodeEndpoint.situation(NodeSituation.SYNC);
            nodeEndpoint.transmit(channel);
            future.complete(null);
        }).clientAction(CommunicationClientAction.FAILED, it -> {
            Node.instance().logger().info("Cluster cannot bind " + nodeEndpoint.data().id() + ". Because endpoint is offline!");
            nodeEndpoint.situation(NodeSituation.NOT_AVAILABLE);
            future.complete(null);
        });
        client.initialize();
        return future;
    }
}