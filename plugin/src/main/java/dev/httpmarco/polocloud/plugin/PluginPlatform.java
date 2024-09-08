package dev.httpmarco.polocloud.plugin;

import dev.httpmarco.polocloud.api.packet.resources.services.ServiceOnlinePacket;
import dev.httpmarco.polocloud.instance.ClusterInstance;

public class PluginPlatform {

    public void presentServiceAsOnline() {
        ClusterInstance.instance().client().sendPacket(new ServiceOnlinePacket(ClusterInstance.instance().selfServiceId()));
    }
}
