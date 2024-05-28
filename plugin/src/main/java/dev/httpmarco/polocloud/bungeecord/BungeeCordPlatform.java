package dev.httpmarco.polocloud.bungeecord;

import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.service.CloudServiceOnlineEvent;
import dev.httpmarco.polocloud.api.events.service.CloudServiceShutdownEvent;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetSocketAddress;

public final class BungeeCordPlatform extends Plugin {

    @Override
    public void onEnable() {
        //todo duplicated code with velocity platform
        var instance = CloudAPI.instance();

        ProxyServer.getInstance().getConfigurationAdapter().getServers().clear();
        ProxyServer.getInstance().getServers().clear();

        instance.globalEventNode().addListener(CloudServiceOnlineEvent.class, startEvent -> {
            if (startEvent.cloudService().group().platform().proxy()) {
                return;
            }
            registerServer(startEvent.cloudService().name(), startEvent.cloudService().port());
        });

        instance.globalEventNode().addListener(CloudServiceShutdownEvent.class, shutdownEvent -> {
            ProxyServer.getInstance().getServers().remove(shutdownEvent.cloudService().name());
        });

        for (var service : instance.serviceProvider().filterService(ServiceFilter.SERVERS)) {
            registerServer(service.name(), service.port());
        }

        CloudInstance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(CloudInstance.SERVICE_ID, ServiceState.ONLINE));
    }

    private void registerServer(String name, int port) {
        final var info = ProxyServer.getInstance().constructServerInfo(
                name,
                new InetSocketAddress("127.0.0.1", port),
                "NextCluster Service",
                false
        );
        ProxyServer.getInstance().getServers().put(name, info);
    }

}
