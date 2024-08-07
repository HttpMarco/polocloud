package dev.httpmarco.polocloud.api.packet.resources.services;

import java.util.UUID;

public final class ServiceConnectPacket extends AbstractClusterServiceIdPacket {

    public ServiceConnectPacket(UUID id) {
        super(id);
    }
}
