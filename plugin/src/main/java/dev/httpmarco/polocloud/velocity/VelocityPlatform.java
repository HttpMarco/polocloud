package dev.httpmarco.polocloud.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.AbstractPlatform;
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
public final class VelocityPlatform extends AbstractPlatform<ProxyServer> {

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
            if (service.name().equalsIgnoreCase("lobby-1")) {
                server.registerServer(new ServerInfo(service.name(),  new InetSocketAddress("127.0.0.1", service.port())));
                System.out.println("Register new service: " + service.name());
            }
        }
    }

    @Subscribe
    public void onProxyInitialize(PlayerChooseInitialServerEvent event) {
        //todo
        event.setInitialServer(server.getAllServers().stream().findFirst().orElse(null));
    }

}
