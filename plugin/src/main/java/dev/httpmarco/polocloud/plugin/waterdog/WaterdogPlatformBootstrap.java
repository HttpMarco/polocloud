package dev.httpmarco.polocloud.plugin.waterdog;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.ProxyPlatformParameterAdapter;
import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public final class WaterdogPlatformBootstrap extends Plugin implements ProxyPlatformParameterAdapter<ProxiedPlayer> {

    private ProxyPluginPlatform<ProxiedPlayer> platform;

    @Override
    public void onEnable() {
        this.platform = new ProxyPluginPlatform<>(new WaterdogPlatformAction(this.getProxy()), new WaterdogServerHandler(this.getProxy()), this);

        new WaterdogPlatformListeners(this.getProxy(), this.platform);
        this.platform.presentServiceAsOnline();
    }

    @Override
    public boolean hasPermission(@NotNull ProxiedPlayer player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public int onlinePlayers() {
        return ProxyServer.getInstance().getPlayerManager().getPlayerCount();
    }

}
