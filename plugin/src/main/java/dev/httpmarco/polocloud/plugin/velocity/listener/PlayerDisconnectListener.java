package dev.httpmarco.polocloud.plugin.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerUnregisterPacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.jetbrains.annotations.NotNull;

public final class PlayerDisconnectListener {

    @Subscribe
    public void onDisconnect(@NotNull DisconnectEvent event) {
        ClusterInstance.instance().client().sendPacket(new PlayerUnregisterPacket(event.getPlayer().getUniqueId()));
    }
}
