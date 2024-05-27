package dev.httpmarco.polocloud.base.events;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.Event;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.events.EventRunnable;
import dev.httpmarco.polocloud.api.events.Listener;
import dev.httpmarco.polocloud.api.packets.event.CloudEventCallPacket;
import dev.httpmarco.polocloud.api.packets.event.CloudEventRegitserPacket;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.services.CloudServiceImpl;
import dev.httpmarco.polocloud.base.services.LocalCloudService;

import java.util.ArrayList;
import java.util.List;

public class GlobalEventNode implements EventNode {


    public GlobalEventNode() {
        CloudBase.instance().transmitter().listen(CloudEventRegitserPacket.class, (transmit, packet) -> {
            // find channel cloud service
            var service = (CloudServiceImpl) CloudAPI.instance().serviceProvider().find(packet.serviceId());
            // register the new event
            service.subscribedEvents().add(packet.event());
        });
    }

    public void call(Event event) {
        for (var cloudService : CloudAPI.instance().serviceProvider().services()) {
            var service = (CloudServiceImpl) cloudService;

            if (service.subscribedEvents().isEmpty()) {
                continue;
            }

            for (var subscribedEvent : service.subscribedEvents()) {
                // call

                if(!event.getClass().getName().equals(subscribedEvent)) {
                    continue;
                }

                if (service instanceof LocalCloudService localCloudService) {
                    localCloudService.channelTransmit().sendPacket(new CloudEventCallPacket(event));
                } else {
                    //todo send information to the current node
                }
            }
        }
    }

    public <T extends Event> void addListener(Class<T> event, EventRunnable<T> runnable) {
        //todo internal
    }
}
