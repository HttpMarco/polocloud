package dev.httpmarco.polocloud.bridges.velocity;

import com.google.common.eventbus.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceOnlineEvent;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceStopEvent;
import org.slf4j.Logger;
import com.google.inject.Inject;

import java.net.InetSocketAddress;

@Plugin(id = "polocloud-bridge", name = "Polocloud-Bridge", version = "1.0.0-SNAPSHOT",
        url = "https://github.com/HttpMarco/polocloud", description = "Apply platform features", authors = {"HttpMarco"})
public final class VelocityBootstrap {

    private final ProxyServer server;

    @Inject
    public VelocityBootstrap(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        var provider = Polocloud.instance().eventProvider();
        provider.subscribe(ClusterServiceOnlineEvent.class, it -> {
            server.registerServer(
                    new ServerInfo(it.service().name(),
                    new InetSocketAddress(it.service().hostname(), it.service().port()))
            );
        });

        provider.subscribe(ClusterServiceStopEvent.class,
                it -> server.getServer(it.service().name()).ifPresent(
                        serverInfo -> server.unregisterServer(serverInfo.getServerInfo())
                )
        );
    }
}
