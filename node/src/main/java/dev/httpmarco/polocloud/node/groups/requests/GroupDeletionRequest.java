package dev.httpmarco.polocloud.node.groups.requests;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.polocloud.api.packet.MessageResponsePacket;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class GroupDeletionRequest {

    public static String TAG = "group-deletion";

    public CompletableFuture<Optional<String>> request(ClusterProvider clusterProvider, String name) {
        var groupFuture = new CompletableFuture<Optional<String>>();

        clusterProvider.headNode().transmit().requestAsync(GroupDeletionRequest.TAG, MessageResponsePacket.class, new CommunicationProperty().set("name", name)).whenComplete((it, t) -> groupFuture.complete(it.successfully() ? Optional.empty() : Optional.of(it.reason()))
        );

        return groupFuture;
    }
}
