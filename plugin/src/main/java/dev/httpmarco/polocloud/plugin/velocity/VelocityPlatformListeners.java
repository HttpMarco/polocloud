package dev.httpmarco.polocloud.plugin.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.PlatformValueChecker;
import dev.httpmarco.polocloud.plugin.PluginPermissions;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class VelocityPlatformListeners {

    private final ProxyServer server;
    private final ProxyPluginPlatform<Player> platform;

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
        if (PlatformValueChecker.reachMaxPlayers(platform, event.getPlayer())) {
            event.getPlayer().disconnect(Component.text("§cThe service is full!"));
            return;
        }
        if (PlatformValueChecker.maintenanceEnabled(platform, event.getPlayer())) {
            event.getPlayer().disconnect(Component.text("§cThe service is in maintenance!"));
            return;
        }
        this.platform.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void serverConnectedEvent(@NotNull ServerPreConnectEvent event) {
        var serverOptional = event.getResult();

        serverOptional.getServer().ifPresent(server -> {
            var service = ClusterInstance.instance().serviceProvider().find(server.getServerInfo().getName());
            if (PlatformValueChecker.reachMaxPlayers(server.getPlayersConnected().size(), service.maxPlayers(), platform, event.getPlayer())) {
                //todo no good player output
                System.out.println(service.onlinePlayersCount() + ":" + server.getPlayersConnected().size());
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
                return;
            }

            if (PlatformValueChecker.maintenanceEnabled(service, platform, event.getPlayer())) {
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
