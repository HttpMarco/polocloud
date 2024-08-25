package dev.httpmarco.polocloud.api.packet.resources.screen;

import dev.httpmarco.polocloud.api.packet.resources.services.AbstractClusterServiceIdPacket;

import java.util.UUID;

public final class ExternalScreenSubscribePacket extends AbstractClusterServiceIdPacket {

    public ExternalScreenSubscribePacket(UUID id) {
        super(id);
    }
}
