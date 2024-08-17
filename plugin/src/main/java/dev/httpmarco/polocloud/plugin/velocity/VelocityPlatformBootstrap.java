package dev.httpmarco.polocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;

@Plugin(id = "polocloud", name = "PoloCloud-Plugin", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformBootstrap {

    private final ProxyServer server;
    private final ProxyPluginPlatform platform;

    @Inject
    public VelocityPlatformBootstrap(ProxyServer server) {
        this.server = server;
        this.platform = new ProxyPluginPlatform(new VelocityPlatformAction(this.server), new VelocityProxyServerHandler(this.server));
    }

    @Subscribe(order = PostOrder.FIRST)
    public void prepareProxy(ProxyInitializeEvent proxyEvent) {
        server.getEventManager().register(this, new VelocityPlatformListeners(this.server, this.platform));
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyInitialize(ProxyInitializeEvent event) {
        platform.presentServiceAsOnline();
    }
}
