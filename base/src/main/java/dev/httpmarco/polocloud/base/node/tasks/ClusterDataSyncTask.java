package dev.httpmarco.polocloud.base.node.tasks;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.node.endpoints.ExternalNodeEndpoint;
import dev.httpmarco.polocloud.base.node.packets.NodeValidationSyncPacket;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@UtilityClass
@Deprecated
public final class ClusterDataSyncTask {

    public static @NotNull CompletableFuture<Boolean> run(@NotNull ExternalNodeEndpoint externalNodeEndpoint, String token) {
        var future = new CompletableFuture<Boolean>();

        externalNodeEndpoint.transmit().request("cluster-data-sync", new CommunicationProperty()
                .set("id", Node.instance().nodeModel().localNode().id())
                .set("hostname", Node.instance().nodeModel().localNode().hostname())
                .set("port", Node.instance().nodeModel().localNode().port())
                .set("token", token)
                .set("bindNode", externalNodeEndpoint.data().id()), NodeValidationSyncPacket.class, packet -> {

            if (packet.successfully()) {
                var nodeModel = Node.instance().nodeModel();
                nodeModel.cluster().id(packet.clusterId());
                nodeModel.cluster().token(token);
                nodeModel.save();
            }

            future.complete(packet.successfully());
        });

        return future;
    }
}
