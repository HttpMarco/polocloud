package dev.httpmarco.polocloud.api.players;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.Sendable;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class ClusterPlayerProvider implements Sendable<ClusterPlayer> {

    public abstract CompletableFuture<Integer> playersCountAsync();

    public abstract CompletableFuture<List<ClusterPlayer>> playersAsync();

    public abstract CompletableFuture<ClusterPlayer> findAsync(UUID uuid);

    public abstract CompletableFuture<ClusterPlayer> findAsync(String name);

    @Override
    public void write(@NotNull ClusterPlayer value, @NotNull PacketBuffer buffer) {
        buffer.writeString(value.name());
        buffer.writeUniqueId(value.uniqueId());
        buffer.writeString(value.currentServerName());
        buffer.writeString(value.currentProxyName());
    }

    @SneakyThrows
    public ClusterPlayer find(String name) {
        return findAsync(name).get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public ClusterPlayer find(UUID uuid) {
        return findAsync(uuid).get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public List<ClusterPlayer> players() {
        return playersAsync().get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public Integer playersCount() {
        return playersCountAsync().get(5, TimeUnit.SECONDS);
    }
}