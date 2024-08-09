package dev.httpmarco.polocloud.api.packet.resources.services;

import java.util.UUID;

public final class ServiceOnlinePacket extends AbstractClusterServiceIdPacket {

    public ServiceOnlinePacket(UUID id) {
        super(id);
    }
}
