package dev.httpmarco.polocloud.bridge.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.sdk.java.Polocloud;
import dev.httpmarco.polocloud.sdk.java.events.definitions.ServiceOnlineEvent;
import dev.httpmarco.polocloud.sdk.java.events.definitions.ServiceShutdownEvent;
import dev.httpmarco.polocloud.sdk.java.services.ServiceType;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.Objects;

@Plugin(id = "polocloud-bridge", name = "Polocloud-Bridge", version = "3.0.0.BETA", authors = {"Polocloud"}, url = "https://github.com/HttpMarco/polocloud", description = "Polocloud-Bridge")
public final class VelocityBridge {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityBridge(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        // remove all registered servers on startup
        for (var registeredServer : server.getAllServers()) {
            server.unregisterServer(registeredServer.getServerInfo());
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Polocloud.Companion.instance().eventProvider().subscribe(ServiceOnlineEvent.class, it -> {
            var service = it.getService();

            if(service.getType() != ServiceType.SERVER) {
                return;
            }

            server.registerServer(new ServerInfo(service.getName(), new InetSocketAddress(service.getHostname(), service.getPort())));
        });

        Polocloud.Companion.instance().eventProvider().subscribe(ServiceShutdownEvent.class, it -> {
            var service = it.getService();

            if(service.getType() != ServiceType.SERVER) {
                return;
            }
            server.getServer(service.getName()).ifPresent(registeredServer -> server.unregisterServer(registeredServer.getServerInfo()));
        });
    }

    @Subscribe
    public void onConnect(PlayerChooseInitialServerEvent event) {
        event.setInitialServer(Objects.requireNonNull(server.getAllServers().stream().findFirst().orElse(null)));
    }
}

