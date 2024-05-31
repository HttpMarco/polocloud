package dev.httpmarco.polocloud;

import dev.httpmarco.polocloud.api.packets.service.CloudServiceStateChangePacket;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.runner.CloudInstance;

import java.util.function.Function;

public class RunningPlatform {

    public RunningPlatform(Function<Void, Integer> calculateOnlinePlayers) {
        CloudInstance.instance().client().transmitter().registerResponder("service-player-request", (channel, it) -> {
            //todo
            calculateOnlinePlayers.apply(null);
            return null;
        });
    }

    public void changeToOnline() {
        CloudInstance.instance().client().transmitter().sendPacket(new CloudServiceStateChangePacket(CloudInstance.SERVICE_ID, ServiceState.ONLINE));
    }
}
