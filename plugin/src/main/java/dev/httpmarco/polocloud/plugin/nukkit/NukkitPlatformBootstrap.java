package dev.httpmarco.polocloud.plugin.nukkit;

import cn.nukkit.plugin.PluginBase;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.PluginPlatform;

public final class NukkitPlatformBootstrap extends PluginBase {

    private final PluginPlatform platform = new PluginPlatform();

    @Override
    public void onEnable() {
        platform.presentServiceAsOnline();
    }
}
