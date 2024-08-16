package dev.httpmarco.polocloud.plugin.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

@AllArgsConstructor
public final class ServerKickListener {

    private ProxyServer server;

    @Subscribe
    public void handelKick(KickedFromServerEvent event) {
        var fallback = ClusterInstance.instance().serviceProvider().find(ClusterServiceFilter.LOWEST_FALLBACK);
        var message = MiniMessage.miniMessage().deserialize("<red>No server available!");
        if (fallback.isEmpty()) {
            event.setResult(KickedFromServerEvent.DisconnectPlayer.create(message));
            return;
        }

        if (!event.getPlayer().isActive()) {
            return;
        }

        fallback.stream().filter(it -> !it.name().equalsIgnoreCase(event.getServer().getServerInfo().getName())).flatMap(service -> server.getServer(service.name()).stream())
                .findFirst()
                .ifPresent(registeredServer -> {
                    if (event.getServer().getServerInfo().getName().equals(registeredServer.getServerInfo().getName())) {
                        event.setResult(KickedFromServerEvent.Notify.create(event.getServerKickReason().orElse(message)));
                    } else {
                        event.setResult(KickedFromServerEvent.RedirectPlayer.create(registeredServer));
                    }
                });
    }
}
