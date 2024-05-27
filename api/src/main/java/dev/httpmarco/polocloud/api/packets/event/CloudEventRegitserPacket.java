package dev.httpmarco.polocloud.api.packets.event;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudEventRegitserPacket extends Packet {

    private String event;

    @Override
    public void onRead(CodecBuffer codecBuffer) {
        this.event = codecBuffer.readString();
    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeString(event);
    }
}
