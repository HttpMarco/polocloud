package dev.httpmarco.polocloud.plugin.spigot;

import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlatformBootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(ClusterInstance.instance().selfServiceId()));
    }

    @Override
    public void onDisable() {

    }
}
