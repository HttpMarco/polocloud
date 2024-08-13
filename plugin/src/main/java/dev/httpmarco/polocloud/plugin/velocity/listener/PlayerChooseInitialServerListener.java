package dev.httpmarco.polocloud.plugin.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

public final class PlayerChooseInitialServerListener {

    private final ProxyServer server;

    public PlayerChooseInitialServerListener(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onPlayerChooseInitialServer(@NotNull PlayerChooseInitialServerEvent event) {
        event.setInitialServer(server.getAllServers().stream().findFirst().orElse(null));
    }
}
