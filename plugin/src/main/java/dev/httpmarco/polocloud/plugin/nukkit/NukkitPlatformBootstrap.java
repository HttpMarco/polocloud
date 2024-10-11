package dev.httpmarco.polocloud.plugin.nukkit;

import cn.nukkit.plugin.PluginBase;
import dev.httpmarco.polocloud.api.ClassSupplier;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.PluginPlatform;

public final class NukkitPlatformBootstrap extends PluginBase implements ClassSupplier {

    private final PluginPlatform platform = new PluginPlatform();

    @Override
    public void onEnable() {
        platform.presentServiceAsOnline();

        CloudAPI.classSupplier(this);
    }

    @Override
    public Class<?> classByName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}
