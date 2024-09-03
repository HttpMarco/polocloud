package dev.httpmarco.polocloud.plugin.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class VelocityPlatformListeners {

    private final ProxyServer server;
    private final ProxyPluginPlatform platform;

    @Subscribe
    public void onPlayerChooseInitialServer(@NotNull PlayerChooseInitialServerEvent event) {
        var fallback = CloudAPI.instance().serviceProvider().find(ClusterServiceFilter.LOWEST_FALLBACK);
        if (fallback.isEmpty()) {
            event.setInitialServer(null);
            return;
        }
        server.getServer(fallback.get(0).name()).ifPresent(event::setInitialServer);
    }

    @Subscribe
    public void onDisconnect(@NotNull DisconnectEvent event) {
        this.platform.unregisterPlayer(event.getPlayer().getUniqueId());
    }

    @Subscribe(order = PostOrder.LATE)
    public void onPostLogin(@NotNull PostLoginEvent event) {

        if (server.getPlayerCount() >= ClusterInstance.instance().selfService().maxPlayers()) {
            event.getPlayer().disconnect(Component.text("&cThe service is full!"));
            return;
        }

        this.platform.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void serverConnectedEvent(@NotNull ServerPreConnectEvent event) {
        var serverOptional = event.getResult();

        serverOptional.getServer().ifPresent(server -> {
            if (server.getPlayersConnected().size() >= ClusterInstance.instance().serviceProvider().find(server.getServerInfo().getName()).maxPlayers()) {
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
            }
        });
    }

    @Subscribe
    public void serverConnectedEvent(@NotNull ServerConnectedEvent event) {
        this.platform.playerChangeServer(event.getPlayer().getUniqueId(), event.getServer().getServerInfo().getName());
    }

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
