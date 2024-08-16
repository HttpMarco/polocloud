package dev.httpmarco.polocloud.plugin.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import org.jetbrains.annotations.NotNull;

public final class PlayerChooseInitialServerListener {

    private final ProxyServer server;

    public PlayerChooseInitialServerListener(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onPlayerChooseInitialServer(@NotNull PlayerChooseInitialServerEvent event) {
        var fallback = CloudAPI.instance().serviceProvider().find(ClusterServiceFilter.LOWEST_FALLBACK);

        if (fallback.isEmpty()) {
            event.setInitialServer(null);
            return;
        }

        server.getServer(fallback.get(0).name()).ifPresent(event::setInitialServer);
    }
}
