package dev.httpmarco.polocloud.plugin.waterdog;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.InitialServerConnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.TransferCompleteEvent;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import org.jetbrains.annotations.NotNull;

public final class WaterdogPlatformListeners {

    private final ProxyServer server;
    private final ProxyPluginPlatform platform;

    public WaterdogPlatformListeners(@NotNull ProxyServer server, ProxyPluginPlatform platform) {
        server.getEventManager().subscribe(PlayerDisconnectEvent.class, this::handleDisconnect);
        server.getEventManager().subscribe(PlayerLoginEvent.class, this::handleConnect);
        server.getEventManager().subscribe(TransferCompleteEvent.class, this::handleTransfer);
        server.getEventManager().subscribe(InitialServerConnectedEvent.class, this::handleInitialize);
        this.platform = platform;
        this.server = server;
    }

    public void handleConnect(@NotNull PlayerLoginEvent event) {

        if (server.getPlayers().size() >= ClusterInstance.instance().selfService().maxPlayers()) {
            event.getPlayer().disconnect("&cThe service is full!");
            return;
        }

        this.platform.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    public void handleDisconnect(@NotNull PlayerDisconnectEvent event) {
        this.platform.unregisterPlayer(event.getPlayer().getUniqueId());
    }

    public void handleTransfer(@NotNull TransferCompleteEvent event) {
        var serverInfo = event.getNewClient().getServerInfo();

        if(serverInfo.getPlayers().size() >= ClusterInstance.instance().serviceProvider().find(serverInfo.getServerName()).maxPlayers()) {
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
