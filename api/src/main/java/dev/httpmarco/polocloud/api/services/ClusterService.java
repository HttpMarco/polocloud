package dev.httpmarco.polocloud.api.services;

import dev.httpmarco.polocloud.api.ChannelAppender;
import dev.httpmarco.polocloud.api.Detail;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import lombok.SneakyThrows;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface ClusterService extends Named, Detail, ChannelAppender {

    ClusterGroup group();

    int orderedId();

    UUID id();

    String hostname();

    int port();

    String runningNode();

    void shutdown();

    void executeCommand(String command);

    ClusterServiceState state();

    List<String> logs();

    void update();

    int maxPlayers();

    CompletableFuture<Integer> onlinePlayersCountAsync();

    CompletableFuture<List<ClusterPlayer>> onlinePlayersAsync();

    CompletableFuture<Integer> currentMemoryAsync();

    PropertiesPool properties();

    default boolean isEmpty() {
        return this.onlinePlayersCount() == 0;
    }

    @SneakyThrows
    default int currentMemory(){
        return this.currentMemoryAsync().get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    default int onlinePlayersCount(){
        return this.onlinePlayersCountAsync().get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    default List<ClusterPlayer> onlinePlayers(){
        return this.onlinePlayersAsync().get(5, TimeUnit.SECONDS);
    }

    @Override
    default String name() {
        return group().name() + "-" + orderedId();
    }
}
