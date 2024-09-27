package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

public final class GroupUpdatePacket extends AbstractGroupPacket {

    public GroupUpdatePacket(ClusterGroup group) {
        super(group);
    }
}
