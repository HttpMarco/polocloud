package dev.httpmarco.polocloud.api.packet;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractStringComponentPacket extends Packet {

    private String content;

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.content = packetBuffer.readString();
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeString(content);
    }
}
