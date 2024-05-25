package dev.httpmarco.polocloud.api.packets;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;

public final class CloudComponentPacketHelper {

    public static void writeService(CloudService cloudService, CodecBuffer buffer) {
        buffer.writeInt(cloudService.orderedId());
    }

    public static CloudService readService(CodecBuffer buffer) {
        return CloudAPI.instance().serviceProvider().fromPacket(buffer);
    }

    public static void writeGroup(CloudGroup cloudService, CodecBuffer buffer) {
        //todo
    }

    public static CloudGroup readGroup(CodecBuffer buffer) {
        return CloudAPI.instance().groupProvider().fromPacket(buffer);
    }

}
