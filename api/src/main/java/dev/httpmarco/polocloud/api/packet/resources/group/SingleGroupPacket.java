package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class SingleGroupPacket extends AbstractGroupPacket {

    public SingleGroupPacket(ClusterGroup group) {
        super(group);
    }
}
