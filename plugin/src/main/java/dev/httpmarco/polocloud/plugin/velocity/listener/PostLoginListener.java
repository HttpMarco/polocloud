package dev.httpmarco.polocloud.plugin.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import dev.httpmarco.polocloud.api.packet.resources.player.PlayerRegisterPacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.jetbrains.annotations.NotNull;

public final class PostLoginListener {

    @Subscribe(order = PostOrder.LATE)
    public void onPostLogin(@NotNull PostLoginEvent event) {
        var player = event.getPlayer();

        ClusterInstance.instance().client().sendPacket(new PlayerRegisterPacket(player.getUniqueId(), player.getUsername(), ClusterInstance.instance().selfServiceId()));
    }

}
