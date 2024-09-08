package dev.httpmarco.polocloud.plugin.waterdog;

import dev.httpmarco.polocloud.plugin.ProxyPluginPlatform;
import dev.waterdog.waterdogpe.plugin.Plugin;

public final class WaterdogPlatformBootstrap extends Plugin {

    private ProxyPluginPlatform platform;

    @Override
    public void onEnable() {
        this.platform = new ProxyPluginPlatform(new WaterdogPlatformAction(this.getProxy()), new WaterdogServerHandler(this.getProxy()));

        new WaterdogPlatformListeners(this.getProxy(), this.platform);
        this.platform.presentServiceAsOnline();
    }
}
