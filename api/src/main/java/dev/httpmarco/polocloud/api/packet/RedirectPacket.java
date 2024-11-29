package dev.httpmarco.polocloud.api.packet;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketAllocator;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class RedirectPacket extends Packet {

    private UUID target;
    private String packetClassName;
    private PacketBuffer buffer;

    public RedirectPacket(UUID target, Packet packet) {
        this.target = target;
        this.packetClassName = packet.getClass().getName();
        this.buffer = PacketBuffer.allocate();
        packet.write(this.buffer);
    }

    public Packet buildPacket() throws ClassNotFoundException {
        var packet = (Packet) PacketAllocator.allocate(Class.forName(this.packetClassName));
        packet.read(new PacketBuffer(this.buffer.getOrigin().copy()));
        return packet;
    }

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.target = packetBuffer.readUniqueId();
        this.packetClassName = packetBuffer.readString();

        var readable = packetBuffer.readInt();
        var bytes = new byte[readable];
        packetBuffer.getOrigin().readBytes(bytes, 0, readable);

        this.buffer = PacketBuffer.allocate(readable);
        this.buffer.getOrigin().writeBytes(bytes);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(this.target);
        packetBuffer.writeString(this.packetClassName);
        packetBuffer.writeInt(this.buffer.getOrigin().readableBytes());
        packetBuffer.writeBytes(this.buffer.getOrigin().copy());
    }
}
