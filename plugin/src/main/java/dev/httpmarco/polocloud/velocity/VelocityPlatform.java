package dev.httpmarco.polocloud.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.Getter;

import javax.inject.Inject;
import java.net.InetSocketAddress;

@Getter
@Plugin(
        id = "polocloud",
        name = "PoloCloud",
        version = "1.0.0",
        authors = "HttpMarco"
)
public final class VelocityPlatform {

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

        for (CloudService service : CloudAPI.instance().serviceProvider().services()) {
            server.registerServer(new ServerInfo(service.name(),  new InetSocketAddress("127.0.0.1", service.port())));
            System.out.println("Register new service: " + service.name());
        }
    }

    @Subscribe
    public void onProxyInitialize(PlayerChooseInitialServerEvent event) {
        event.setInitialServer(server.getAllServers().stream()
                .filter(it -> it.getServerInfo().getName().toLowerCase().startsWith("lobby"))
                .findFirst()
                .orElse(null)
        );
    }

}
