package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class AbstractGroupPacket extends Packet {

    private ClusterGroup group;

    @Override
    public void read(PacketBuffer packetBuffer) {
        this.group = CloudAPI.instance().groupProvider().read(packetBuffer);
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        CloudAPI.instance().groupProvider().write(group, packetBuffer);
    }

}
