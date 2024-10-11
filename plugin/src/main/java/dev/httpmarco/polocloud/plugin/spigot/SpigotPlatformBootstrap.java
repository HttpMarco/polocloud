package dev.httpmarco.polocloud.plugin.spigot;

import dev.httpmarco.polocloud.api.ClassSupplier;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.plugin.PluginPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlatformBootstrap extends JavaPlugin implements ClassSupplier {

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
