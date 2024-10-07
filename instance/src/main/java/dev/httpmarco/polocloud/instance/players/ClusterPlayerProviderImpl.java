package dev.httpmarco.polocloud.instance.players;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.BooleanPacket;
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
    public @NotNull CompletableFuture<Boolean> onlineAsync(UUID uuid) {
        var future = new CompletableFuture<Boolean>();
        ClusterInstance.instance().client().requestAsync("player-online", BooleanPacket.class, new CommunicationProperty().set("uuid", uuid)).whenComplete((it, t) -> future.complete(it.value()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> onlineAsync(String name) {
        var future = new CompletableFuture<Boolean>();
        ClusterInstance.instance().client().requestAsync("player-online", BooleanPacket.class, new CommunicationProperty().set("name", name)).whenComplete((it, t) -> future.complete(it.value()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<Integer> playersCountAsync() {
        var future = new CompletableFuture<Integer>();
        ClusterInstance.instance().client().requestAsync("player-count", IntPacket.class).whenComplete((it, t) ->  future.complete(it.value()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterPlayer>> playersAsync() {
        var future = new CompletableFuture<List<ClusterPlayer>>();
        ClusterInstance.instance().client().requestAsync("player-all", PlayerCollectionPacket.class).whenComplete((it, t) -> future.complete(it.players()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(UUID uuid) {
        var future = new CompletableFuture<ClusterPlayer>();
        ClusterInstance.instance().client().requestAsync("player-find", PlayerPacket.class, new CommunicationProperty().set("uuid", uuid)).whenComplete((it, t) -> future.complete(it.player()));
        return future;
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(String name) {
        var future = new CompletableFuture<ClusterPlayer>();
        ClusterInstance.instance().client().requestAsync("player-find", PlayerPacket.class, new CommunicationProperty().set("name", name)).whenComplete((it, t) -> future.complete(it.player()));
        return future;
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterPlayer read(@NotNull PacketBuffer buffer) {
        return new ClusterPlayerImpl(buffer.readString(), buffer.readUniqueId(), buffer.readString(), buffer.readString());
    }
}
