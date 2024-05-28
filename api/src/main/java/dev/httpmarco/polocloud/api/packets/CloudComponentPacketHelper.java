package dev.httpmarco.polocloud.api.packets;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;

public final class CloudComponentPacketHelper {

    public static void writeService(CloudService cloudService, CodecBuffer buffer) {
        writeGroup(cloudService.group(), buffer);

        buffer.writeInt(cloudService.orderedId());
        buffer.writeUniqueId(cloudService.id());
        buffer.writeInt(cloudService.port());
        buffer.writeEnum(cloudService.state());
        //todo properties
    }

    public static CloudService readService(CodecBuffer buffer) {
        //todo properties
        return CloudAPI.instance().serviceProvider().fromPacket(readGroup(buffer), buffer);
    }

    public static void writeGroup(CloudGroup group, CodecBuffer buffer) {
        buffer.writeString(group.name());
        buffer.writeString(group.platform().version());
        buffer.writeBoolean(group.platform().proxy());
        buffer.writeInt(group.minOnlineServices());
        buffer.writeInt(group.memory());

        //todo properties
    }

    public static CloudGroup readGroup(CodecBuffer buffer) {
        //todo properties
        return CloudAPI.instance().groupProvider().fromPacket(buffer);
    }
}
