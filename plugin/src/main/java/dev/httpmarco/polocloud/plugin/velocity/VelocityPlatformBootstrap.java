package dev.httpmarco.polocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceOnlineEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@Slf4j
@Plugin(id = "polocloud", name = "PoloCloud-Plugin", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformBootstrap {

    private final ProxyServer server;

    @Inject
    public VelocityPlatformBootstrap(ProxyServer server) {
        this.server = server;
    }


    @Subscribe(order = PostOrder.FIRST)
    public void prepareProxy(ProxyInitializeEvent proxyEvent) {
        for (var registered : this.server.getAllServers()) {
            this.server.unregisterServer(registered.getServerInfo());
        }

        CloudAPI.instance().eventProvider().listen(ServiceOnlineEvent.class, event -> {
            if (event.service().group().platform().type() == PlatformType.SERVER) {
                server.registerServer(new ServerInfo(event.service().name(), new InetSocketAddress(event.service().hostname(), event.service().port())));
            }
        });

        CloudAPI.instance().eventProvider().listen(ServiceStoppingEvent.class, event -> {
            server.getServer(event.service().name()).ifPresent(registeredServer -> server.unregisterServer(registeredServer.getServerInfo()));
        });

        // todo add filter
        for (var service : CloudAPI.instance().serviceProvider().services()) {
            if (service.group().platform().type() == PlatformType.SERVER) {
                server.registerServer(new ServerInfo(service.name(), new InetSocketAddress(service.hostname(), service.port())));
            }
        }
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyInitialize(ProxyInitializeEvent event) {
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(ClusterInstance.instance().selfServiceId()));
    }

    @Subscribe
    public void onPlayerChooseInitialServer(@NotNull PlayerChooseInitialServerEvent event) {
        event.setInitialServer(server.getAllServers().stream().findFirst().orElse(null));
    }
}
