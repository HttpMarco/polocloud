package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class AbstractGroupPacket extends Packet {

    private ClusterGroup group;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        var nullResult = packetBuffer.readBoolean();

        if(!nullResult) {
            this.group = CloudAPI.instance().groupProvider().read(packetBuffer);
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(this.group == null);

        if(this.group != null) {
            CloudAPI.instance().groupProvider().write(group, packetBuffer);
        }
    }
}
