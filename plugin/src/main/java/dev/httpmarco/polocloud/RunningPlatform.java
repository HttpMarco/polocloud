package dev.httpmarco.polocloud;

import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;

public class RunningPlatform {

    public void changeToOnline() {
        CloudInstance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(CloudInstance.SERVICE_ID, ServiceState.ONLINE));
    }
}
