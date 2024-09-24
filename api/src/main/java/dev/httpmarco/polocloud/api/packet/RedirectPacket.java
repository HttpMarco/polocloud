package dev.httpmarco.polocloud.api.packet;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class RedirectPacket extends Packet {

    private final Packet packet;

    @Override
    public void read(PacketBuffer packetBuffer) {

    }

    @Override
    public void write(PacketBuffer packetBuffer) {

    }
}
