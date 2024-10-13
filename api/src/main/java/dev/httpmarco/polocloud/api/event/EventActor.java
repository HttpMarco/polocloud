package dev.httpmarco.polocloud.api.event;

import dev.httpmarco.polocloud.api.packet.resources.event.EventCallPacket;

public interface EventActor {

    void alert(EventCallPacket packet);

}
