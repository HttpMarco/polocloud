package dev.httpmarco.polocloud.addons.proxy.bungeecord;

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCordBootstrap extends Plugin {

    @Override
    public void onEnable() {

        new ProxyAddon();

        getProxy().getPluginManager().registerCommand(this, new BungeeCordProxyCommand());
        getProxy().getPluginManager().registerListener(this, new BungeeCordListener());
    }
}
