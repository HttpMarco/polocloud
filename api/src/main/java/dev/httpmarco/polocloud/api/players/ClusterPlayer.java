package dev.httpmarco.polocloud.api.players;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.SneakyThrows;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface ClusterPlayer extends Named {

    UUID uniqueId();

    String currentProxyName();

    String currentServerName();

    CompletableFuture<ClusterService> currentProxyAsync();

    CompletableFuture<ClusterService> currentServerAsync();

    @SneakyThrows
    default ClusterService currentProxy(){
        return this.currentProxyAsync().get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    default ClusterService currentServer(){
        return this.currentServerAsync().get(5, TimeUnit.SECONDS);
    }
}