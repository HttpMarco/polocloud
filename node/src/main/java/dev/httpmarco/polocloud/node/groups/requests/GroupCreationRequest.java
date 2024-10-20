package dev.httpmarco.polocloud.node.groups.requests;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.polocloud.api.packet.MessageResponsePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Log4j2
@UtilityClass
public class GroupCreationRequest {

    public static String TAG = "group-creation";

    public CompletableFuture<Optional<String>> request(@NotNull ClusterProvider clusterProvider, String name, String[] nodes, @NotNull PlatformGroupDisplay platform, int maxMemory, boolean staticService, int minOnline, int maxOnline) {
        var groupFuture = new CompletableFuture<Optional<String>>();
        clusterProvider.headNode().requestAsync(GroupCreationRequest.TAG, MessageResponsePacket.class, new CommunicationProperty()
                        .set("name", name)
                        .set("nodes", JsonUtils.GSON.toJson(nodes))
                        .set("platform", platform.platform())
                        .set("version", platform.version())
                        .set("maxMemory", maxMemory)
                        .set("staticService", staticService)
                        .set("minOnline", minOnline)
                        .set("maxOnline", maxOnline)
                ).whenComplete((it, t) -> groupFuture.complete(it.successfully() ? Optional.empty() : Optional.of(it.reason())));
        return groupFuture;
    }

}
