package dev.httpmarco.polocloud.instance.groups;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupCollectionPacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ClusterInstanceGroupProvider extends ClusterGroupProvider {

    @Override
    public @NotNull CompletableFuture<Set<ClusterGroup>> groupsAsync() {
        var future = new CompletableFuture<Set<ClusterGroup>>();
        ClusterInstance.instance().client().request("groups-all", GroupCollectionPacket.class, it -> future.complete(it.groups()));
        return future;
    }

    @Override
    public CompletableFuture<Boolean> existsAsync(String group) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<String>> deleteAsync(String group) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<String>> createAsync(String name, String[] nodes, PlatformGroupDisplay platform, int minMemory, int maxMemory, boolean staticService, int minOnline, int maxOnline) {
        return null;
    }

    @Override
    public CompletableFuture<ClusterGroup> findAsync(@NotNull String group) {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public ClusterGroup read(PacketBuffer buffer) {
        return null;
    }
}
