package dev.httpmarco.polocloud.api.events.service;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.packets.CloudComponentPacketHelper;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudServiceStartEvent implements ServiceEvent{

    private CloudService cloudService;

    @Override
    public void read(CodecBuffer buffer) {
        this.cloudService = CloudComponentPacketHelper.readService(buffer);
    }

    @Override
    public void write(CodecBuffer buffer) {
        CloudComponentPacketHelper.writeService(cloudService, buffer);
    }
}
