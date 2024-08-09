package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class GroupCollectionPacket extends Packet {

    private Set<ClusterGroup> groups;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        var amount = packetBuffer.readInt();

        this.groups = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            this.groups.add(CloudAPI.instance().groupProvider().read(packetBuffer));
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.groups.size());
        for (ClusterGroup group : groups) {
            CloudAPI.instance().groupProvider().write(group, packetBuffer);
        }
    }
}
