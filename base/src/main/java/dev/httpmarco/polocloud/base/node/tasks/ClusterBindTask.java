package dev.httpmarco.polocloud.base.node.tasks;

import dev.httpmarco.osgan.networking.CommunicationFuture;
import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.osgan.networking.client.CommunicationClientAction;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import dev.httpmarco.polocloud.base.node.data.NodeData;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.packets.NodeSituationCallbackPacket;
import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public final class ClusterBindTask {

    public CompletableFuture<ExternalNodeEndpoint> merge(String id, String hostname, int port, String token) {

        var endpoint = new ExternalNodeEndpoint(new NodeData(id, hostname, port));
        var future = new CommunicationFuture<ExternalNodeEndpoint>();
        var client = new CommunicationClient(hostname, port);

        client.clientAction(CommunicationClientAction.CONNECTED, channelTransmit -> {
            endpoint.situation(NodeSituation.DETECT_SIT);
            endpoint.transmit(channelTransmit);

            channelTransmit.request("cluster-node-situation", new CommunicationProperty(), NodeSituationCallbackPacket.class, packet -> {
                endpoint.situation(packet.nodeSituation());
                future.complete(endpoint);
            });
        });

        client.clientAction(CommunicationClientAction.FAILED, channelTransmit -> {
            endpoint.situation(NodeSituation.NOT_AVAILABLE);
            future.complete(endpoint);
        });

        client.initialize();
        return future;
    }
}