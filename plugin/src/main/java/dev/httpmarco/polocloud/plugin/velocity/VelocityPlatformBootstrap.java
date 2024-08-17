package dev.httpmarco.polocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceOnlineEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerActionBarPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerConnectPacket;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerMessagePacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.velocity.listener.*;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.minimessage.MiniMessage;

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

        server.getEventManager().register(this, new PostLoginListener());
        server.getEventManager().register(this, new ServerConnectedListener());
        server.getEventManager().register(this, new PlayerDisconnectListener());
        server.getEventManager().register(this, new ServerKickListener(this.server));
        server.getEventManager().register(this, new PlayerChooseInitialServerListener(this.server));

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

        ClusterInstance.instance().client().listen(PlayerMessagePacket.class, (transmit, packet) -> server.getPlayer(packet.uuid()).ifPresent(player -> player.sendMessage(MiniMessage.miniMessage().deserialize(packet.message()))));
        ClusterInstance.instance().client().listen(PlayerActionBarPacket.class, (transmit, packet) -> server.getPlayer(packet.uuid()).ifPresent(player -> player.sendActionBar(MiniMessage.miniMessage().deserialize(packet.message()))));
        ClusterInstance.instance().client().listen(PlayerConnectPacket.class, (transmit, packet) -> server.getPlayer(packet.uuid()).ifPresent(player -> server.getServer(packet.serverId()).ifPresent(it -> player.createConnectionRequest(it).fireAndForget())));


        for (var service : CloudAPI.instance().serviceProvider().find(ClusterServiceFilter.ONLINE_SERVICES)) {
            if (service.group().platform().type() == PlatformType.SERVER) {
                var hostname = service.hostname();
                if (service.runningNode().equalsIgnoreCase(ClusterInstance.instance().selfService().runningNode())) {
                    hostname = "127.0.0.1";
                }
                server.registerServer(new ServerInfo(service.name(), new InetSocketAddress(hostname, service.port())));
            }
        }
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyInitialize(ProxyInitializeEvent event) {
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(ClusterInstance.instance().selfServiceId()));
    }
}
