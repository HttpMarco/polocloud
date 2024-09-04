package dev.httpmarco.polocloud.plugin.fabric;

import dev.httpmarco.polocloud.instance.ClusterInstance;
import dev.httpmarco.polocloud.plugin.PluginPlatform;
import net.fabricmc.api.ModInitializer;

public final class FabricPlatformBootstrap implements ModInitializer {

    private final PluginPlatform platform = new PluginPlatform();

    @Override
    public void onInitialize() {

        System.out.println("global: " + ClassLoader.getSystemClassLoader());
        System.out.println("instance: " + ClusterInstance.class.getClassLoader());
        System.out.println("test: " + this.getClass().getClassLoader());

        platform.presentServiceAsOnline();
    }
}
