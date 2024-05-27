package dev.httpmarco.polocloud.api.packets.event;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudEventRegitserPacket extends Packet {

    private UUID serviceId;
    private String event;

    @Override
    public void onRead(CodecBuffer codecBuffer) {
        this.serviceId = codecBuffer.readUniqueId();
        this.event = codecBuffer.readString();
    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(serviceId);
        codecBuffer.writeString(event);
    }
}
