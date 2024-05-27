package dev.httpmarco.polocloud.api.packets.service;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Accessors(fluent = true)
public class CloudServiceRegisterPacket extends Packet {

    private UUID uuid;

    public CloudServiceRegisterPacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void onRead(CodecBuffer codecBuffer) {
        this.uuid = codecBuffer.readUniqueId();
    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(this.uuid);
    }
}
