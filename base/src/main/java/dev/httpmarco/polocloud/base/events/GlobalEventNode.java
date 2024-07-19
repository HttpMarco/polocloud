/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.base.events;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.Event;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.events.EventRunnable;
import dev.httpmarco.polocloud.api.packets.event.CloudEventCallPacket;
import dev.httpmarco.polocloud.api.packets.event.CloudEventRegitserPacket;
import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.services.CloudServiceImpl;
import dev.httpmarco.polocloud.base.services.LocalCloudService;

public class GlobalEventNode implements EventNode {

    public GlobalEventNode() {
        Node.instance().transmitter().listen(CloudEventRegitserPacket.class, (transmit, packet) -> {
            // find channel cloud serviceasd
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
