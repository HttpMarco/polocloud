package dev.httpmarco.polocloud.addons.proxy.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCordBootstrap extends Plugin {

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeCordProxyCommand());
    }

    @Override
    public void onDisable() {

    }
}
