package dev.httpmarco.polocloud.api.packet;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketAllocator;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
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
    private Packet packet;

    @Override
    @SneakyThrows
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.target = packetBuffer.readUniqueId();

        String clazz = packetBuffer.readString();
        this.packet = (Packet) PacketAllocator.allocate(Class.forName(clazz));
        this.packet.read(packetBuffer);
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(target);
        packetBuffer.writeString(packet.getClass().getName());
        packet.write(packetBuffer);
    }
}
