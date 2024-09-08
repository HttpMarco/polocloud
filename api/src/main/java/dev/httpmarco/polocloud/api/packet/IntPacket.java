package dev.httpmarco.polocloud.api.packet;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor
public class IntPacket extends Packet {

    private int value;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.value = packetBuffer.readInt();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(value);
    }
}
