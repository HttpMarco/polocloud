package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceOnlineEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.bungeecord.listener.PlayerDisconnectListener;
import dev.httpmarco.polocloud.plugin.bungeecord.listener.PlayerPostLoginListener;
import dev.httpmarco.polocloud.plugin.bungeecord.listener.PlayerPreLoginListener;
import dev.httpmarco.polocloud.plugin.bungeecord.listener.ServerConnectedListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetSocketAddress;

public final class BungeeCordPlatformBootstrap extends Plugin {

    @Override
    public void onEnable() {
        var instance = ProxyServer.getInstance();
        instance.getConfigurationAdapter().getServers().clear();
        instance.getServers().clear();

        var pluginManager = instance.getPluginManager();
        pluginManager.registerListener(this, new PlayerDisconnectListener());
        pluginManager.registerListener(this, new PlayerPreLoginListener());
        pluginManager.registerListener(this, new ServerConnectedListener());
        pluginManager.registerListener(this, new PlayerPostLoginListener());


        instance.setReconnectHandler(new BungeecordReconnectHandler());

        CloudAPI.instance().eventProvider().listen(ServiceOnlineEvent.class, event -> {
            if (event.service().group().platform().type() == PlatformType.SERVER) {
                registerServer(event.service().name(), event.service().hostname(), event.service().port());
            }
        });

        CloudAPI.instance().eventProvider().listen(ServiceStoppingEvent.class, event -> instance.getServers().remove(event.service().name()));

        for (var service : CloudAPI.instance().serviceProvider().find(ClusterServiceFilter.ONLINE_SERVICES)) {
            if (service.group().platform().type() == PlatformType.SERVER) {
                registerServer(service.name(), service.hostname(), service.port());
            }
        }

        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(ClusterInstance.instance().selfServiceId()));
    }

    private void registerServer(String name, String hostname, int port) {
        var info = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(hostname, port), "PoloCloud Service", false);
        ProxyServer.getInstance().getServers().put(name, info);
    }
}