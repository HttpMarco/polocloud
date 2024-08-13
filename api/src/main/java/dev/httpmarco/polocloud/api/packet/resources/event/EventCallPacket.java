package dev.httpmarco.polocloud.api.packet.resources.event;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.event.util.PacketAllocator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class EventCallPacket extends Packet {

    private Event event;

    @Override
    @SneakyThrows
    public void read(PacketBuffer packetBuffer) {
        var clazz = Class.forName(packetBuffer.readString());

        this.event = (Event) PacketAllocator.allocate(clazz);
        this.event.read(packetBuffer);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(event.getClass().getName());
        event.write(packetBuffer);
    }
}
