package dev.httpmarco.polocloud.plugin.bungeecord;

import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class BungeeCordPlatformListeners implements Listener {

    private final ProxyServer server;
    private final ProxyPluginPlatform platform;

    @EventHandler
    public void handlePlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
        platform.unregisterPlayer(event.getPlayer().getUniqueId());
    }


    @EventHandler
    public void handleServerConnect(@NotNull PostLoginEvent event) {

        var service = ClusterInstance.instance().selfService();
        if(server.getOnlineCount() >= service.maxPlayers()) {
            event.getPlayer().disconnect(new TextComponent("&cThe service is full!"));
            return;
        }

        if (service.properties().has(GroupProperties.MAINTENANCE) && service.properties().property(GroupProperties.MAINTENANCE)) {
            event.getPlayer().disconnect(new TextComponent("&cThe service is in maintenance!"));
            return;
        }

        platform.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    @EventHandler
    public void handleServerConnect(@NotNull ServerConnectEvent event) {
        var service = ClusterInstance.instance().serviceProvider().find(event.getTarget().getName());
        if (event.getTarget().getPlayers().size() >= service.maxPlayers()) {
            event.setCancelled(true);
            return;
        }

        if (service.properties().has(GroupProperties.MAINTENANCE) && service.properties().property(GroupProperties.MAINTENANCE)) {
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