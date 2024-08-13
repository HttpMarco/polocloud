package dev.httpmarco.polocloud.node.players;

import dev.httpmarco.polocloud.api.players.AbstractClusterPlayer;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Accessors(fluent = true)
public final class ClusterPlayerImpl extends AbstractClusterPlayer {

    private final ClusterService currentProxy;

    @Setter
    @Nullable
    private ClusterService currentServer;

    public ClusterPlayerImpl(String name, UUID uniqueId, @NotNull ClusterService currentProxy) {
        super(name, uniqueId,"unknown", currentProxy.name());
        this.currentProxy = currentProxy;
    }

    @Override
    public String currentProxyName() {
        return this.currentProxy.name();
    }

    @Override
    public String currentServerName() {
        return this.currentServer.name();
    }

    @Override
    public CompletableFuture<ClusterService> currentProxyAsync() {
        return CompletableFuture.completedFuture(currentProxy);
    }

    @Override
    public CompletableFuture<ClusterService> currentServerAsync() {
        return CompletableFuture.completedFuture(currentServer);
    }
}
