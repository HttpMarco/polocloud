package dev.httpmarco.polocloud.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.RunningProxyPlatform;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ServiceFilter;
import lombok.Getter;

import javax.inject.Inject;
import java.net.InetSocketAddress;

@Getter
@Plugin(id = "polocloud", name = "PoloCloud", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatform extends RunningProxyPlatform {

    private final ProxyServer server;

    @Inject
    public VelocityPlatform(ProxyServer server) {
        super(unused -> server.getPlayerCount(), it -> server.registerServer(new ServerInfo(it.name(), new InetSocketAddress(it.hostname(), it.port()))),
                it -> server.getServer(it.name()).ifPresent(registeredServer -> server.unregisterServer(registeredServer.getServerInfo())));
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        for (var registered : this.server.getAllServers()) {
            this.server.unregisterServer(registered.getServerInfo());
        }
        this.changeToOnline();
    }

    @Subscribe
    public void onProxyInitialize(PlayerChooseInitialServerEvent event) {
        var service = CloudAPI.instance().serviceProvider().filterService(ServiceFilter.LOWEST_FALLBACK);
        if (service.isEmpty()) {
            event.setInitialServer(null);
            return;
        }
        server.getServer(service.get(0).name()).ifPresentOrElse(event::setInitialServer, () -> event.setInitialServer(null));
    }
}
