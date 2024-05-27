package dev.httpmarco.polocloud.runner.event;

import dev.httpmarco.polocloud.api.events.Event;
import dev.httpmarco.polocloud.api.events.EventNode;
import dev.httpmarco.polocloud.api.events.EventRunnable;
import dev.httpmarco.polocloud.api.packets.event.CloudEventRegitserPacket;
import dev.httpmarco.polocloud.runner.Instance;

public class InstanceGlobalEventNode implements EventNode<Event> {

    @Override
    public void addListener(Class<? extends Event> event, EventRunnable<Event> runnable) {
        Instance.instance().client().transmitter().sendPacket(new CloudEventRegitserPacket(event.getName()));
    }
}
