package dev.httpmarco.polocloud.api.packets.service;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class CloudServiceStateChangePacket extends Packet {

    //todo
    private UUID id;
    private ServiceState state;

    @Override
    public void onRead(CodecBuffer codecBuffer) {
        this.id = codecBuffer.readUniqueId();
        this.state = codecBuffer.readEnum(ServiceState.class);
    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(this.id);
        codecBuffer.writeEnum(this.state);
    }
}
