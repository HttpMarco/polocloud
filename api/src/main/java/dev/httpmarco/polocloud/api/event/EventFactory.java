package dev.httpmarco.polocloud.api.event;

import dev.httpmarco.polocloud.api.packet.resources.event.EventCallPacket;

public interface EventFactory {

    void call(Event event);

    void call(EventCallPacket packet);

}
