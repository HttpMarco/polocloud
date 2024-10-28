package dev.httpmarco.polocloud.api.players;

import dev.httpmarco.polocloud.api.Detail;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface ClusterPlayer extends Named, Detail {

    UUID uniqueId();

    String currentProxyName();

    String currentServerName();

    CompletableFuture<ClusterService> currentProxyAsync();

    CompletableFuture<ClusterService> currentServerAsync();

    void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    void sendTablist(String header, String footer);

    void sendMessage(String message);

    void sendActionBar(String message);

    void connect(String serviceId);

    @SneakyThrows
    default ClusterService currentProxy() {
        return this.currentProxyAsync().get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    default ClusterService currentServer() {
        return this.currentServerAsync().get(5, TimeUnit.SECONDS);
    }

    @Override
    default String details() {
        return "uniqueId&8=&7" + uniqueId() + "&8, &7current proxy&8=&7" + currentProxyName() + ", &7 current server&8=&7" + currentServerName();
    }

    default void sendTitle(String title, String subtitle) {
        //default minecraft values
        this.sendTitle(title, subtitle, 10, 70, 20);
    }

    default void connect(ClusterService service) {
        this.connect(service.name());
    }
}