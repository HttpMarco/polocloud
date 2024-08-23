package dev.httpmarco.polocloud.node.packets.resources.services;

import dev.httpmarco.polocloud.api.packet.resources.services.AbstractClusterServiceIdPacket;
import java.util.UUID;

public final class ClusterSyncUnregisterServicePacket extends AbstractClusterServiceIdPacket {

    public ClusterSyncUnregisterServicePacket(UUID id) {
        super(id);
    }
}
