package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.polocloud.api.packet.AbstractBooleanPacket;

public final class GroupExistsResponsePacket extends AbstractBooleanPacket {

    public GroupExistsResponsePacket(boolean value) {
        super(value);
    }
}
