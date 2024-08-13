package dev.httpmarco.polocloud.api.event;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;

public interface Event {

    void read(PacketBuffer buffer);

    void write(PacketBuffer buffer);

}
