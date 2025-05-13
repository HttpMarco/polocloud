package dev.httpmarco.polocloud.bridges.velocity;

import com.google.common.eventbus.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.platform.PlatformType;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceOnlineEvent;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceStopEvent;
import com.google.inject.Inject;
import org.slf4j.Logger;
import java.net.InetSocketAddress;

@Plugin(id = "polocloud-bridge", name = "Polocloud-Bridge", version = "1.0.0-SNAPSHOT",
        url = "https://github.com/HttpMarco/polocloud", description = "Apply platform features", authors = {"HttpMarco"})
public class VelocityBootstrap {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityBootstrap(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        var provider = Polocloud.instance().eventProvider();
        provider.subscribe(ClusterServiceOnlineEvent.class, it -> registerService(it.service()));

        provider.subscribe(ClusterServiceStopEvent.class,
                it -> server.getServer(it.service().name()).ifPresent(
                        serverInfo -> server.unregisterServer(serverInfo.getServerInfo())
                )
        );

        for (var service : Polocloud.instance().serviceProvider().findAll()) {
            if(service.group().platform().type() != PlatformType.SERVER) {
                continue;
            }

            registerService(service);
            System.out.println("Registered service " + service.name());
        }
    }

    @Subscribe
    public void fallbackTemp(PlayerChooseInitialServerEvent event) {
        event.setInitialServer(server.getServer("lobby-1").orElse(null));
    }

    private void registerService(ClusterService service) {
        server.registerServer(
                new ServerInfo(service.name(),
                        new InetSocketAddress(service.hostname(), service.port()))
        );
    }

}
