package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.endpoints.NodeEndpoint;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class NodeConnectionFactory {

    public static void bindCluster() {
        var headProvider = CloudBase.instance().nodeHeadProvider();

        // start connection of the other nodes
        for (var nodeEndpoint : headProvider.externalNodeEndpoints()) {
            bind(nodeEndpoint);
        }

        // detect head node
        var headNode = headProvider.externalNodeEndpoints().stream().filter(it -> it.situation() == NodeSituation.REACHABLE).map(it -> (NodeEndpoint) it).findFirst().orElse(headProvider.localEndpoint());

        CloudBase.instance().nodeHeadProvider().headNodeEndpoint(headNode);

        if (headNode.equals(headProvider.headNodeEndpoint())) {
            // start factory queue
        }
    }

    public static void bind(@NotNull ExternalNodeEndpoint nodeEndpoint) {

        var client = new CommunicationClient(nodeEndpoint.data().hostname(), nodeEndpoint.data().port());

        client.clientAction(CommunicationClientAction.CONNECTED, it -> {
            // First we must detect the head node of the current cluster
            nodeEndpoint.situation(NodeSituation.SYNC);
        });

        client.clientAction(CommunicationClientAction.DISCONNECTED, it -> {

        });

        client.clientAction(CommunicationClientAction.FAILED, it -> {
            nodeEndpoint.situation(NodeSituation.NOT_AVAILABLE);
        });

        client.clientAction(CommunicationClientAction.CLIENT_DISCONNECT, it -> {

        });
    }
}