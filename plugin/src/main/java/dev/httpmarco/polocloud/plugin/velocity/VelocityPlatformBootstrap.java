package dev.httpmarco.polocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.ProxyPlatformParameterAdapter;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import org.jetbrains.annotations.NotNull;

@Plugin(id = "polocloud", name = "PoloCloud-Plugin", version = "1.0.0", authors = "HttpMarco")
public final class VelocityPlatformBootstrap implements ProxyPlatformParameterAdapter<Player> {
    private final ProxyServer server;
    private final ProxyPluginPlatform<Player> platform;

    @Inject
    public VelocityPlatformBootstrap(ProxyServer server) {
        this.server = server;

        this.platform = new ProxyPluginPlatform<>(new VelocityPlatformAction(this.server), new VelocityProxyServerHandler(this.server), this);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void prepareProxy(ProxyInitializeEvent proxyEvent) {
        server.getEventManager().register(this, new VelocityPlatformListeners(this.server, this.platform));
        server.getCommandManager().register(server.getCommandManager().metaBuilder("cloud").plugin(this).build(), new VelocityCloudCommand());
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyInitialize(ProxyInitializeEvent event) {
        platform.presentServiceAsOnline();
    }

    @Override
    public boolean hasPermission(@NotNull Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public int onlinePlayers() {
        return server.getPlayerCount();
    }

}
