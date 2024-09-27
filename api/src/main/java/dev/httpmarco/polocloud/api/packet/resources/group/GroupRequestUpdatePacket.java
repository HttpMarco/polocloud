package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;

public final class GroupRequestUpdatePacket extends AbstractGroupPacket {

    public GroupRequestUpdatePacket(ClusterGroup group) {
        super(group);
    }
}
