package dev.httpmarco.polocloud.api.packets;

import dev.httpmarco.osgan.networking.Packet;
import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudAllServicesPacket extends Packet {

    private List<CloudService> services;

    public CloudAllServicesPacket(List<CloudService> services) {
        this.services = services;
    }

    @Override
    public void onRead(CodecBuffer codecBuffer) {
        this.services = new ArrayList<>();

        int amount = codecBuffer.readInt();
        for (int i = 0; i < amount; i++) {
            this.services.add(CloudComponentPacketHelper.readService(codecBuffer));
        }
    }

    @Override
    public void onWrite(CodecBuffer codecBuffer) {
        codecBuffer.writeInt(services.size());

        for (CloudService service : services) {
            CloudComponentPacketHelper.writeService(service, codecBuffer);
        }
    }
}
