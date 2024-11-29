package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.ProxyPlatformParameterAdapter;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class BungeeCordPlatformBootstrap extends Plugin implements ProxyPlatformParameterAdapter<ProxiedPlayer> {

    @Override
    public void onEnable() {
        var platform = new ProxyPluginPlatform<>(new BungeeCordPlatformAction(), new BungeeCordPlatformServerHandler(), this);
        var instance = ProxyServer.getInstance();

        instance.getConfigurationAdapter().getServers().clear();
        instance.getPluginManager().registerListener(this, new BungeeCordPlatformListeners(ProxyServer.getInstance(), platform));
        instance.getPluginManager().registerCommand(this, new BungeeCordCloudCommand());
        instance.setReconnectHandler(new BungeeCordReconnectHandler(platform));

        platform.presentServiceAsOnline();
    }

    @Override
    public boolean hasPermission(@NotNull ProxiedPlayer player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public int onlinePlayers() {
        return ProxyServer.getInstance().getOnlineCount();
    }
}