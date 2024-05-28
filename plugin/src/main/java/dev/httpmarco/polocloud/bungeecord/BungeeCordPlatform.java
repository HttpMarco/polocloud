package dev.httpmarco.polocloud.bungeecord;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.service.CloudServiceOnlineEvent;
import dev.httpmarco.polocloud.api.events.service.CloudServiceShutdownEvent;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;

public final class BungeeCordPlatform extends Plugin implements Listener
{

    @Override
    public void onEnable() {
        //todo duplicated code with velocity platform
        var instance = CloudAPI.instance();

        ProxyServer.getInstance().getConfigurationAdapter().getServers().clear();
        ProxyServer.getInstance().getServers().clear();

        instance.globalEventNode().addListener(CloudServiceOnlineEvent.class, startEvent -> {
            if (startEvent.cloudService().group().platform().proxy() || startEvent.cloudService().state() != ServiceState.ONLINE) {
                return;
            }
            registerServer(startEvent.cloudService().name(), startEvent.cloudService().port());
        });

        instance.globalEventNode().addListener(CloudServiceShutdownEvent.class, shutdownEvent -> ProxyServer.getInstance().getServers().remove(shutdownEvent.cloudService().name()));

        for (var service : instance.serviceProvider().filterService(ServiceFilter.SERVERS)) {
            registerServer(service.name(), service.port());
        }

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        CloudInstance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(CloudInstance.SERVICE_ID, ServiceState.ONLINE));
    }

    private void registerServer(String name, int port) {
        final var info = ProxyServer.getInstance().constructServerInfo(
                name,
                new InetSocketAddress("127.0.0.1", port),
                "PoloCloud Service",
                false
        );
        ProxyServer.getInstance().getServers().put(name, info);
    }


    @EventHandler
    public void handle(PreLoginEvent event) {
        if (ProxyServer.getInstance().getServers().isEmpty()) {
            event.setReason(TextComponent.fromLegacy("No fallback server available"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(ServerConnectEvent event) {

        var service = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);

        if (service.isEmpty()) {
            event.getPlayer().disconnect(TextComponent.fromLegacy("No fallback server available"));
            event.setCancelled(true);
            return;
        }

        var info = ProxyServer.getInstance().getServerInfo(service.get(0).name());

        if (info == null) {
            event.getPlayer().disconnect(TextComponent.fromLegacy("No fallback server available"));
            event.setCancelled(true);
            return;
        }
        System.out.println("found fallback: " + service.get(0).name());
        event.setTarget(info);
    }
}
