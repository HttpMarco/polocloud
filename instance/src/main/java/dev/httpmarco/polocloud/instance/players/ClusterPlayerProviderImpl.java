package dev.httpmarco.polocloud.instance.players;

import dev.httpmarco.polocloud.api.players.ClusterPlayer;
import dev.httpmarco.polocloud.api.players.ClusterPlayerProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClusterPlayerProviderImpl extends ClusterPlayerProvider {

    @Override
    public CompletableFuture<ClusterPlayer> findAsync(UUID uuid) {
        return null;
    }

    @Override
    public CompletableFuture<ClusterPlayer> findAsync(String name) {
        return null;
    }
}
