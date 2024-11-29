package dev.httpmarco.polocloud.api.packet.resources.event;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.event.util.PacketAllocator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class EventCallPacket extends Packet {
    private String className;
    private PacketBuffer buffer;

    public EventCallPacket(Event event) {
        this.className = event.getClass().getName();
        this.buffer = PacketBuffer.allocate();
        event.write(this.buffer);
    }

    public Event buildEvent() throws ClassNotFoundException {
        var event = (Event) PacketAllocator.allocate(Class.forName(this.className));
        event.read(new PacketBuffer(this.buffer.getOrigin().copy()));
        return event;
    }

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.className = packetBuffer.readString();

        var readable = packetBuffer.readInt();
        var bytes = new byte[readable];
        packetBuffer.getOrigin().readBytes(bytes, 0, readable);

        this.buffer = PacketBuffer.allocate(readable);
        this.buffer.getOrigin().writeBytes(bytes);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(this.className);
        packetBuffer.writeInt(this.buffer.getOrigin().readableBytes());
        packetBuffer.writeBytes(this.buffer.getOrigin().copy());
    }
}
