package dev.httpmarco.polocloud.plugin.bungeecord.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void handlePreLogin(PreLoginEvent event) {
        if (ProxyServer.getInstance().getServers().isEmpty()) {
            event.setReason(new TextComponent("§cNo fallback server available"));
            event.setCancelled(true);
        }
    }
}
