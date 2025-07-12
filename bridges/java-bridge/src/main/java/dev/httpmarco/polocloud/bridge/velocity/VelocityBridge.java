package dev.httpmarco.polocloud.bridge.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.sdk.java.Polocloud;
import dev.httpmarco.polocloud.sdk.java.groups.Group;
import org.slf4j.Logger;

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
        System.out.println("Amount of registered servers: " + Polocloud.Companion.instance().groupProvider().find().size());
        for (Group group : Polocloud.Companion.instance().groupProvider().find()) {
            System.out.println("Registering group: " + group.name());
        }
    }
}
