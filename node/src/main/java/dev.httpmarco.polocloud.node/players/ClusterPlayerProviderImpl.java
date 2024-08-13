package dev.httpmarco.polocloud.node.players;

import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.api.players.ClusterPlayerProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Accessors(fluent = true)
public final class ClusterPlayerProviderImpl extends ClusterPlayerProvider {

    private final Map<UUID, ClusterPlayer> players = new HashMap<>();

    @Override
    public @NotNull CompletableFuture<Integer> playersCountAsync() {
        return CompletableFuture.completedFuture(players.size());
    }

    @Override
    public @NotNull CompletableFuture<List<ClusterPlayer>> playersAsync() {
        return CompletableFuture.completedFuture(players.values().stream().toList());
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(UUID uuid) {
        return CompletableFuture.completedFuture(this.players.get(uuid));
    }

    @Override
    public @NotNull CompletableFuture<ClusterPlayer> findAsync(String name) {
        return CompletableFuture.completedFuture(this.players.values().stream().filter(it -> it.name().equals(name)).findFirst().orElse(null));
    }
}
