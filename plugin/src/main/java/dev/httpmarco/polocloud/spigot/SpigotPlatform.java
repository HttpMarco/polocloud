package dev.httpmarco.polocloud.spigot;

import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlatform extends JavaPlugin {

    @Override
    public void onEnable() {
        //todo duplicated code
        CloudInstance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(CloudInstance.SERVICE_ID, ServiceState.ONLINE));
    }
}
