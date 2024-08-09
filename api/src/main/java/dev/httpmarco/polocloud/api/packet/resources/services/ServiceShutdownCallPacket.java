package dev.httpmarco.polocloud.api.packet.resources.services;

import java.util.UUID;

public final class ServiceShutdownCallPacket extends AbstractClusterServiceIdPacket{

    public ServiceShutdownCallPacket(UUID id) {
        super(id);
    }
}
