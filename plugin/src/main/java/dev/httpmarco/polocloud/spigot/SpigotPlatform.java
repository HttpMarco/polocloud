package dev.httpmarco.polocloud.spigot;

import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.Instance;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotPlatform extends JavaPlugin {

    @Override
    public void onEnable() {
        //todo duplicated code
        Instance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(Instance.SERVICE_ID, ServiceState.ONLINE));
    }
}
