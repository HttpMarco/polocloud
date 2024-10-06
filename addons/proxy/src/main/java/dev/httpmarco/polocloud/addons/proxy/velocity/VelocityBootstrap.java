package dev.httpmarco.polocloud.addons.proxy.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon;


@Plugin(id = "polocloud-proxy", name = "PoloCloud-Proxy", version = "1.0.11-SNAPSHOT", authors = {"HttpMarco", "RECHERGG"})
public final class VelocityBootstrap {

    private final ProxyServer server;

    @Inject
    public VelocityBootstrap(ProxyServer server) {
        this.server = server;
        new ProxyAddon();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        var commandManager = server.getCommandManager();
        commandManager.register(commandManager.metaBuilder("proxy").plugin(this).build(), new VelocityProxyCommand());

        var eventManager = server.getEventManager();
        eventManager.register(this, new VelocityListener());
    }
}
