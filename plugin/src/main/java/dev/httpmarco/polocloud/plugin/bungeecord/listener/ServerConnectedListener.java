package dev.httpmarco.polocloud.plugin.bungeecord.listener;

import dev.httpmarco.polocloud.api.packet.resources.player.PlayerServiceChangePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectedListener implements Listener {

    @EventHandler
    public void handleServerConnect(ServerSwitchEvent event) {
        var player = event.getPlayer();
        ClusterInstance.instance().client().sendPacket(new PlayerServiceChangePacket(player.getUniqueId(), event.getPlayer().getServer().getInfo().getName()));

    }
}
