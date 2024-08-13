package dev.httpmarco.polocloud.instance.players;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.players.AbstractClusterPlayer;
import dev.httpmarco.polocloud.api.services.ClusterService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ClusterPlayerImpl extends AbstractClusterPlayer {

    public ClusterPlayerImpl(String name, UUID uniqueId, String currentServerName, String currentProxyName) {
        super(name, uniqueId, currentServerName, currentProxyName);
    }

    @Override
    public CompletableFuture<ClusterService> currentProxyAsync() {
        return CloudAPI.instance().serviceProvider().findAsync(currentProxyName());
    }

    @Override
    public CompletableFuture<ClusterService> currentServerAsync() {
        return CloudAPI.instance().serviceProvider().findAsync(currentServerName());
    }
}
