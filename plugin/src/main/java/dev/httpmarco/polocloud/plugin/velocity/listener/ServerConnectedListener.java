package dev.httpmarco.polocloud.plugin.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerServiceChangePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;

public final class ServerConnectedListener {

    @Subscribe
    public void serverConnectedEvent(ServerConnectedEvent event) {
        var player = event.getPlayer();
        ClusterInstance.instance().client().sendPacket(new PlayerServiceChangePacket(player.getUniqueId(), event.getServer().getServerInfo().getName()));
    }
}
