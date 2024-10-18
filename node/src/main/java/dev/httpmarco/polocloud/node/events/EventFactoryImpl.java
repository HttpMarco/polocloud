package dev.httpmarco.polocloud.node.events;

import dev.httpmarco.polocloud.api.event.*;
import dev.httpmarco.polocloud.api.packet.resources.event.EventCallPacket;
import dev.httpmarco.polocloud.node.Node;

public final class EventFactoryImpl implements EventFactory {

    @Override
    public void call(Event event) {
        this.call(new EventCallPacket(event));
    }

    @Override
    public void call(EventCallPacket packet) {
        Node.instance().clusterProvider().broadcast(packet);

        for (var pool : EventPoolRegister.pools()) {
            pool.acceptActor(packet);
        }
    }
}
