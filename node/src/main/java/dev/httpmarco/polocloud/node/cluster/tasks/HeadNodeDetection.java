package dev.httpmarco.polocloud.node.cluster.tasks;

import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import dev.httpmarco.polocloud.node.cluster.NodeSituation;
import dev.httpmarco.polocloud.node.cluster.ClusterProviderImpl;
import dev.httpmarco.polocloud.node.packets.node.NodeHeadRequestPacket;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class HeadNodeDetection {

    public NodeEndpoint detect(@NotNull ClusterProviderImpl service) {

        if (service.endpoints().isEmpty()) {
            return service.localNode();
        }

        for (NodeEndpoint endpoint : service.endpoints()) {
            if (endpoint.situation() == NodeSituation.RECHEABLE) {
                var futureStep = new CompletableFuture<String>();

                // request head node data
                Objects.requireNonNull(endpoint.transmit()).request("node-head-request", NodeHeadRequestPacket.class, (response) -> futureStep.complete(response.headNode()));

                var result = futureStep.join();
                var nodeEndpoint = service.find(result);

                if (nodeEndpoint.situation() != NodeSituation.RECHEABLE || nodeEndpoint.transmit() == null) {
                    //todo here we have a big problem bro
                }
                return nodeEndpoint;
            }
        }
        return service.localNode();
    }
}
