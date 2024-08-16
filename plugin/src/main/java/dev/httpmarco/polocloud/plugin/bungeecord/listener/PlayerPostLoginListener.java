package dev.httpmarco.polocloud.plugin.bungeecord.listener;

import com.velocitypowered.api.event.connection.PostLoginEvent;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerRegisterPacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import net.md_5.bungee.api.plugin.Listener;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public final class PlayerPostLoginListener implements Listener {

    @EventHandler
    public void handleServerConnect(@NotNull PostLoginEvent event) {
        var player = event.getPlayer();

        ClusterInstance.instance().client().sendPacket(new PlayerRegisterPacket(player.getUniqueId(), player.getUsername(), ClusterInstance.instance().selfServiceId()));

    }
}
