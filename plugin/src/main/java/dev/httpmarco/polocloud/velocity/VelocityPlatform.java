package dev.httpmarco.polocloud.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.AbstractPlatform;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.Getter;

import javax.inject.Inject;

@Plugin(
        id = "polocloud",
        name = "PoloCloud",
        version = "1.0.0"
)
public final class VelocityPlatform extends AbstractPlatform<ProxyServer> {

    @Getter
    private final ProxyServer server;

    @Inject
    public VelocityPlatform(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        for (var registered : this.server.getAllServers()) {
            this.server.unregisterServer(registered.getServerInfo());
        }

        //todo register sub servers
        for (CloudService service : CloudAPI.instance().serviceProvider().services()) {
            System.out.println("Register new service: " + service.name());
        }
    }
}
