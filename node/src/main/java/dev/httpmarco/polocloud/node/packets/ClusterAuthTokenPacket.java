package dev.httpmarco.polocloud.node.packets;

import dev.httpmarco.polocloud.api.packet.BooleanPacket;

public final class ClusterAuthTokenPacket extends BooleanPacket {

    public ClusterAuthTokenPacket(boolean value) {
        super(value);
    }
}
