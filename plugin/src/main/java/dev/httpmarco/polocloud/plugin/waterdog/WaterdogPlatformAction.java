package dev.httpmarco.polocloud.plugin.waterdog;

import dev.httpmarco.polocloud.plugin.PluginPlatformAction;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.AllArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
public final class WaterdogPlatformAction implements PluginPlatformAction {

    private final ProxyServer proxyServer;

    @Override
    public void sendMessage(UUID uuid, String message) {
        proxyServer.getPlayer(uuid).sendMessage(message);
    }

    @Override
    public void sendActionbar(UUID uuid, String message) {
        // waterdog not support this feature
    }

    @Override
    public void sendTitle(UUID uuid, String title, String subTitle, int fadeInt, int stay, int fadeOut) {
        proxyServer.getPlayer(uuid).sendTitle(title, subTitle, fadeInt, stay, fadeOut);
    }

    @Override
    public void connect(UUID uuid, String serverId) {
        proxyServer.getPlayer(uuid).connect(proxyServer.getServerInfo(serverId));
    }
}
