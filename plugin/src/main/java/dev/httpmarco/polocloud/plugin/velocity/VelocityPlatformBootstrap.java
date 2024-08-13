package dev.httpmarco.polocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceOnlineEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

        CloudAPI.instance().eventProvider().listen(ServiceOnlineEvent.class, serviceOnlineEvent -> {
           System.out.println("register service " + serviceOnlineEvent.service().name());
        });

        CloudAPI.instance().eventProvider().listen(ServiceStoppingEvent.class, serviceOnlineEvent -> {
            System.out.println("unregister service " + serviceOnlineEvent.service().name());
        });
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyInitialize(ProxyInitializeEvent event) {
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(ClusterInstance.instance().selfServiceId()));
    }
}
