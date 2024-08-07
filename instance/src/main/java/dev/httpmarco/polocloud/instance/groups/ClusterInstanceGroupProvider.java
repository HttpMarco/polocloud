package dev.httpmarco.polocloud.instance.groups;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ClusterInstanceGroupProvider extends ClusterGroupProvider {

    @Override
    public CompletableFuture<Set<ClusterGroup>> groupsAsync() {
        return null;
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
