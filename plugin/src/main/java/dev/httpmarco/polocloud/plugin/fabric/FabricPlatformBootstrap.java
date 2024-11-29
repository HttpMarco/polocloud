package dev.httpmarco.polocloud.plugin.fabric;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.PluginPlatform;
import net.fabricmc.api.ModInitializer;

public final class FabricPlatformBootstrap implements ModInitializer {

    private final PluginPlatform platform = new PluginPlatform();

    @Override
    public void onInitialize() {
        platform.presentServiceAsOnline();
    }
}
