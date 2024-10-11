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
        var packet = (Packet) PacketAllocator.allocate(CloudAPI.instance().classByName(this.packetClassName));
        packet.read(this.buffer);
        return packet;
    }

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.target = packetBuffer.readUniqueId();
        this.packetClassName = packetBuffer.readString();
        this.buffer = new PacketBuffer(packetBuffer.getOrigin().copy());
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(this.target);
        packetBuffer.writeString(this.packetClassName);
        packetBuffer.writeBytes(this.buffer.getOrigin());
    }
}
