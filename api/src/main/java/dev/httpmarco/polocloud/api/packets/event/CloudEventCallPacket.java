package dev.httpmarco.polocloud.api.packets.event;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.osgan.reflections.common.Allocator;
import dev.httpmarco.polocloud.api.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class CloudEventCallPacket extends Packet {

    private Event event;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void onRead(CodecBuffer codecBuffer) {
        event = Allocator.allocate((Class<Event>) Class.forName(codecBuffer.readString()));
        event.read(codecBuffer);

    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeString(event.getClass().getName());
        event.write(codecBuffer);
    }
}
