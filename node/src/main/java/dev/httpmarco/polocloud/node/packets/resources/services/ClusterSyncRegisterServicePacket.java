package dev.httpmarco.polocloud.node.packets.resources.services;

import dev.httpmarco.polocloud.api.packet.resources.services.ClusterServicePacket;
import dev.httpmarco.polocloud.api.services.ClusterService;

public final class ClusterSyncRegisterServicePacket extends ClusterServicePacket {

    public ClusterSyncRegisterServicePacket(ClusterService service) {
        super(service);
    }
}
