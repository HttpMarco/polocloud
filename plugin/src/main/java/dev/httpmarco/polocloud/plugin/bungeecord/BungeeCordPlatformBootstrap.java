package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCordPlatformBootstrap extends Plugin {

    @Override
    public void onEnable() {

        var platform = new ProxyPluginPlatform(new BungeeCordPlatformAction(), new BungeeCordPlatformServerHandler());
        var instance = ProxyServer.getInstance();

        instance.getConfigurationAdapter().getServers().clear();
        instance.getPluginManager().registerListener(this, new BungeeCordPlatformListeners(platform));
        instance.setReconnectHandler(new BungeeCordReconnectHandler());

        platform.presentServiceAsOnline();
    }
}