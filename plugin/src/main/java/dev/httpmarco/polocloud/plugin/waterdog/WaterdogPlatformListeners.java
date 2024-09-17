package dev.httpmarco.polocloud.plugin.waterdog;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.PlatformValueChecker;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.InitialServerConnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.TransferCompleteEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

public final class WaterdogPlatformListeners {

    private final ProxyServer server;
    private final ProxyPluginPlatform<ProxiedPlayer> platform;

    public WaterdogPlatformListeners(@NotNull ProxyServer server, ProxyPluginPlatform<ProxiedPlayer> platform) {
        server.getEventManager().subscribe(PlayerDisconnectEvent.class, this::handleDisconnect);
        server.getEventManager().subscribe(PlayerLoginEvent.class, this::handleConnect);
        server.getEventManager().subscribe(TransferCompleteEvent.class, this::handleTransfer);
        server.getEventManager().subscribe(InitialServerConnectedEvent.class, this::handleInitialize);
        this.platform = platform;
        this.server = server;
    }

    public void handleConnect(@NotNull PlayerLoginEvent event) {
        if (PlatformValueChecker.reachMaxPlayers(platform, event.getPlayer())) {
            event.getPlayer().disconnect("§cThe service is full!");
            return;
        }

        if (PlatformValueChecker.reachMaxPlayers(platform, event.getPlayer())) {
            event.getPlayer().disconnect("§cThe service is in maintenance!");
            return;
        }

        this.platform.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    public void handleDisconnect(@NotNull PlayerDisconnectEvent event) {
        this.platform.unregisterPlayer(event.getPlayer().getUniqueId());
    }

    public void handleTransfer(@NotNull TransferCompleteEvent event) {
        var serverInfo = event.getNewClient().getServerInfo();

        var service = ClusterInstance.instance().serviceProvider().find(serverInfo.getServerName());
        if (PlatformValueChecker.reachMaxPlayers(serverInfo.getPlayers().size(), service.maxPlayers(), platform, event.getPlayer())) {
            event.setCancelled();
            return;
        }

        if (PlatformValueChecker.maintenanceEnabled(service, platform, event.getPlayer())) {
            event.setCancelled();
            event.setCancelled();
            return;
        }

        this.platform.playerChangeServer(event.getPlayer().getUniqueId(), serverInfo.getServerName());
    }

    public void handleInitialize(@NotNull InitialServerConnectedEvent event) {

        var fallback = CloudAPI.instance().serviceProvider().find(ClusterServiceFilter.LOWEST_FALLBACK);
        if (fallback.isEmpty()) {
            event.setCancelled();
            return;
        }
        event.getPlayer().connect(server.getServerInfo(fallback.get(0).name()));
        this.platform.playerChangeServer(event.getPlayer().getUniqueId(), event.getInitialDownstream().getServerInfo().getServerName());
    }
}
