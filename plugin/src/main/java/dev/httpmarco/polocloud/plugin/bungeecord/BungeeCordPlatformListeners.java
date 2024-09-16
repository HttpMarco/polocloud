package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.PlatformValueChecker;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class BungeeCordPlatformListeners implements Listener {

    private final ProxyServer server;
    private final ProxyPluginPlatform<ProxiedPlayer> platform;

    @EventHandler
    public void handlePlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
        platform.unregisterPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void handleServerConnect(@NotNull PostLoginEvent event) {
        if (PlatformValueChecker.reachMaxPlayers(platform, event.getPlayer())) {
            event.getPlayer().disconnect(new TextComponent("§cThe service is full!"));
            return;
        }

        if (PlatformValueChecker.reachMaxPlayers(platform, event.getPlayer())) {
            event.getPlayer().disconnect(new TextComponent("§cThe service is in maintenance!"));
            return;
        }

        platform.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    @EventHandler
    public void handleServerConnect(@NotNull ServerConnectEvent event) {
        var service = ClusterInstance.instance().serviceProvider().find(event.getTarget().getName());

        if (PlatformValueChecker.reachMaxPlayers(event.getTarget().getPlayers().size(), service.maxPlayers(), platform, event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        if (PlatformValueChecker.maintenanceEnabled(service, platform, event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleServerConnect(@NotNull ServerSwitchEvent event) {
        platform.playerChangeServer(event.getPlayer().getUniqueId(), event.getPlayer().getServer().getInfo().getName());
    }

    @EventHandler
    public void handlePreLogin(PreLoginEvent event) {
        if (ProxyServer.getInstance().getServers().isEmpty()) {
            event.setReason(new TextComponent("§cNo fallback server available"));
            event.setCancelled(true);
        }
    }
}