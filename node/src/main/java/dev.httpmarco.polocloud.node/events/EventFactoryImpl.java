package dev.httpmarco.polocloud.node.events;

import dev.httpmarco.polocloud.api.event.*;
import dev.httpmarco.polocloud.api.packet.resources.event.EventCallPacket;
import dev.httpmarco.polocloud.node.Node;

public final class EventFactoryImpl implements EventFactory {

    @Override
    public void call(Event event) {
        Node.instance().clusterProvider().broadcast(new EventCallPacket(event));

        for (var pool : EventPoolRegister.pools()) {
            pool.acceptActor(event);
        }
    }
}
