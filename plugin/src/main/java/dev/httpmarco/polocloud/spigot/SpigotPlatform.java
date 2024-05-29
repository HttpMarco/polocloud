package dev.httpmarco.polocloud.spigot;

import dev.httpmarco.polocloud.RunningPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlatform extends JavaPlugin {

    private final RunningPlatform runningPlatform = new RunningPlatform();

    @Override
    public void onEnable() {
        runningPlatform.changeToOnline();
    }
}
