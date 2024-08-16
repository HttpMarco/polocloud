package dev.httpmarco.polocloud.plugin.bungeecord;

import dev.httpmarco.polocloud.api.services.ClusterServiceFilter;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class BungeecordReconnectHandler implements ReconnectHandler {

    @Override
    public ServerInfo getServer(ProxiedPlayer proxiedPlayer) {
        var service = ClusterInstance.instance().serviceProvider().find(ClusterServiceFilter.LOWEST_FALLBACK);

        if (service.isEmpty()) {
            proxiedPlayer.disconnect(TextComponent.fromLegacy("No fallback found"));
            return null;
        }
        return ProxyServer.getInstance().getServerInfo(service.get(0).name());
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
