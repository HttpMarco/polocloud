package dev.httpmarco.polocloud.node.players;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.BooleanPacket;
import dev.httpmarco.polocloud.api.packet.IntPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.*;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.api.players.ClusterPlayerProvider;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.NodeProperties;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Slf4j
@Accessors(fluent = true)
public final class ClusterPlayerProviderImpl extends ClusterPlayerProvider {

    private final Map<UUID, ClusterPlayer> playersPool = new HashMap<>();

    public ClusterPlayerProviderImpl() {
        var servicedProvider = Node.instance().serviceProvider();
        Node.instance().server().listen(PlayerRegisterPacket.class, (transmit, packet) -> {
            if (!servicedProvider.isServiceChannel(transmit)) {
                Node.instance().clusterProvider().broadcast(packet);
            }

            var proxy = servicedProvider.find(packet.proxy());

            if (Node.instance().nodeProperties().has(NodeProperties.LOG_PLAYERS_CONNECTION) && Node.instance().nodeProperties().property(NodeProperties.LOG_PLAYERS_CONNECTION)) {
                log.info("The player &8'&7{}&8' &7is connected on the proxy {}&8.", packet.username(), proxy.name());
            }
            playersPool.put(packet.uuid(), new ClusterPlayerImpl(packet.username(), packet.uuid(), proxy));
        });

        Node.instance().server().listen(PlayerServiceChangePacket.class, (transmit, packet) -> {
            if (!servicedProvider.isServiceChannel(transmit)) {
                Node.instance().clusterProvider().broadcast(packet);
            }

            var player = find(packet.uuid());

            if (player == null) {
                log.warn("This node cannot change the current server of player: {}", packet.uuid());
                return;
            }
            ((ClusterPlayerImpl) player).currentServer(Node.instance().serviceProvider().find(packet.service()));

            if (Node.instance().nodeProperties().has(NodeProperties.LOG_PLAYERS_SERVER_SWITCH) && Node.instance().nodeProperties().property(NodeProperties.LOG_PLAYERS_SERVER_SWITCH)) {
                log.info("The player &8'&7{}&8' &7switched to {}&8.", player.name(), packet.service());
            }
        });

        Node.instance().server().listen(PlayerUnregisterPacket.class, (transmit, packet) -> {
            if (!servicedProvider.isServiceChannel(transmit)) {
                Node.instance().clusterProvider().broadcast(packet);
            }
            var player = find(packet.uuid());
            if (Node.instance().nodeProperties().has(NodeProperties.LOG_PLAYERS_DISCONNECTION) && Node.instance().nodeProperties().property(NodeProperties.LOG_PLAYERS_DISCONNECTION)) {
                log.info("The player &8'&7{}&8' &7disconnected", player.name());
            }
            playersPool.remove(packet.uuid());
        });

        Node.instance().server().registerResponder("player-all", property -> new PlayerCollectionPacket(players()));
        Node.instance().server().registerResponder("player-count", property -> new IntPacket(playersCount()));
        Node.instance().server().registerResponder("player-find", property -> new PlayerPacket(property.has("uuid") ? find(property.getUUID("uuid")) : find(property.getString("name"))));
        Node.instance().server().registerResponder("player-online", property -> new BooleanPacket(property.has("uuid") ? online(property.getUUID("uuid")) : online(property.getString("name"))));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> onlineAsync(UUID uuid) {
        return CompletableFuture.completedFuture(this.playersPool.containsKey(uuid));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> onlineAsync(String name) {
        return CompletableFuture.completedFuture(playersPool.values().stream().anyMatch(it -> it.name().equalsIgnoreCase(name)));
    }

    @Override
    public @NotNull CompletableFuture<Integer> playersCountAsync() {
        return CompletableFuture.completedFuture(playersPool.size());
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterPlayer>> playersAsync() {
        return CompletableFuture.completedFuture(playersPool.values().stream().toList());
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(UUID uuid) {
        return CompletableFuture.completedFuture(this.playersPool.get(uuid));
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(String name) {
        return CompletableFuture.completedFuture(this.playersPool.values().stream().filter(it -> it.name().equals(name)).findFirst().orElse(null));
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterPlayer read(@NotNull PacketBuffer buffer) {
        return new ClusterPlayerImpl(buffer.readString(),
                buffer.readUniqueId(),
                Node.instance().serviceProvider().find(buffer.readString()),
                Node.instance().serviceProvider().find(buffer.readString()));
    }
}