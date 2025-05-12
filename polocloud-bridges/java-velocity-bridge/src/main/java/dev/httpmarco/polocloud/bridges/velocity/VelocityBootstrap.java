package dev.httpmarco.polocloud.bridges.velocity;

import com.google.common.eventbus.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceOnlineEvent;
import dev.httpmarco.polocloud.api.services.events.ClusterServiceStopEvent;
import org.slf4j.Logger;
import com.google.inject.Inject;

@Plugin(id = "polocloud-bridge", name = "Polocloud-Bridge", version = "1.0.0-SNAPSHOT",
        url = "https://github.com/HttpMarco/polocloud", description = "Apply platform features", authors = {"HttpMarco"})
public final class VelocityBootstrap {

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
        provider.subscribe(ClusterServiceOnlineEvent.class, it -> {
            // todo implement
        });

        provider.subscribe(ClusterServiceStopEvent.class, it -> {
            // todo implement
        });
    }
}
