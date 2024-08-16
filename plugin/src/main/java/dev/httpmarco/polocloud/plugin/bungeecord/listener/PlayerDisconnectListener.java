package dev.httpmarco.polocloud.plugin.bungeecord.listener;

import dev.httpmarco.polocloud.api.packet.resources.player.PlayerUnregisterPacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public final class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void handlePlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
        ClusterInstance.instance().client().sendPacket(new PlayerUnregisterPacket(event.getPlayer().getUniqueId()));
    }
}
