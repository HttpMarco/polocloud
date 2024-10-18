package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public final class BungeeCordReconnectHandler implements ReconnectHandler {

    private final ProxyPluginPlatform<ProxiedPlayer> platform;

    @Override
    public ServerInfo getServer(ProxiedPlayer proxiedPlayer) {
        var service = this.platform.findFallback();

        if (service.isEmpty()) {
            proxiedPlayer.disconnect(TextComponent.fromLegacy("No fallback found"));
            return null;
        }
        return ProxyServer.getInstance().getServerInfo(service.get().name());
    }

    @Override
    public void setServer(ProxiedPlayer proxiedPlayer) {
    }

    @Override
    public void save() {
    }

    @Override
    public void close() {
    }
}
