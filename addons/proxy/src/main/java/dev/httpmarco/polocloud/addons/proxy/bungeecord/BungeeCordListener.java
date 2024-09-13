package dev.httpmarco.polocloud.addons.proxy.bungeecord;

import dev.httpmarco.polocloud.addons.proxy.ProxyAddon;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public final class BungeeCordListener implements Listener {

    @EventHandler
    public void handle(@NotNull ProxyPingEvent event) {
        var response = event.getResponse();
        var players = response.getPlayers();

        players.setOnline(ProxyAddon.instance().getOnline());

        if (ProxyAddon.instance().hasMaxPlayers()) {
            players.setMax(ProxyAddon.instance().getMaxPlayers());
        }

        response.setPlayers(players);
        event.setResponse(response);
    }
}
