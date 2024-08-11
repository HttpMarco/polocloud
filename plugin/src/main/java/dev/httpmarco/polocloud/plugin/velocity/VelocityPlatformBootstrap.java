package dev.httpmarco.polocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;

import java.util.UUID;

@Plugin(id = "polocloud", name = "PoloCloud-Plugin", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformBootstrap {

    private final ProxyServer server;

    @Inject
    public VelocityPlatformBootstrap(ProxyServer server) {
        this.server = server;
    }


    @Subscribe(order = PostOrder.FIRST)
    public void prepareProxy(ProxyInitializeEvent event) {
        for (var registered : this.server.getAllServers()) {
            this.server.unregisterServer(registered.getServerInfo());
        }
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyInitialize(ProxyInitializeEvent event) {
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(UUID.fromString(System.getenv("serviceId"))));
    }
}
