package dev.httpmarco.polocloud.addons.proxy.velocity;


import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import dev.httpmarco.polocloud.addons.proxy.ProxyAddon;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class VelocityListener {

    @Subscribe
    public void handle(ProxyPingEvent event) {
        var response = event.getPing();
        var proxyAddon = ProxyAddon.instance();

        // todo add maintenance tag on players

        var responseBuilder = response.asBuilder()
                .onlinePlayers(proxyAddon.getOnline())
                .maximumPlayers(proxyAddon.getMaxPlayers())
                .description(MiniMessage.miniMessage().deserialize(proxyAddon.maintenance() ? proxyAddon.maintenanceMotd().toString() : proxyAddon.motd().toString()));

        event.setPing(responseBuilder.build());
    }

}
