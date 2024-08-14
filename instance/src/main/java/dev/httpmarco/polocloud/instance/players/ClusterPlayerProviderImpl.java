package dev.httpmarco.polocloud.instance.players;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.IntPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerCollectionPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerPacket;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.api.players.ClusterPlayerProvider;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ClusterPlayerProviderImpl extends ClusterPlayerProvider {

    @Override
    public CompletableFuture<Boolean> onlineAsync(UUID uuid) {
        //todo
        return null;
    }

    @Override
    public CompletableFuture<Boolean> onlineAsync(String name) {
        //todo
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Integer> playersCountAsync() {
        var future = new CompletableFuture<Integer>();
        ClusterInstance.instance().client().request("player-count", IntPacket.class, packet -> future.complete(packet.value()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterPlayer>> playersAsync() {
        var future = new CompletableFuture<List<ClusterPlayer>>();
        ClusterInstance.instance().client().request("player-all", PlayerCollectionPacket.class, packet -> future.complete(packet.players()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(UUID uuid) {
        var future = new CompletableFuture<ClusterPlayer>();
        ClusterInstance.instance().client().request("player-find", new CommunicationProperty().set("uuid", uuid), PlayerPacket.class, packet -> future.complete(packet.player()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(String name) {
        var future = new CompletableFuture<ClusterPlayer>();
        ClusterInstance.instance().client().request("player-find", new CommunicationProperty().set("name", name), PlayerPacket.class, packet -> future.complete(packet.player()));
        return future;
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterPlayer read(@NotNull PacketBuffer buffer) {
        return new ClusterPlayerImpl(buffer.readString(), buffer.readUniqueId(), buffer.readString(), buffer.readString());
    }
}
