package dev.httpmarco.polocloud.api.packet.resources.group;

import dev.httpmarco.polocloud.api.packet.AbstractStringComponentPacket;

public final class GroupDeletePacket extends AbstractStringComponentPacket {

    public GroupDeletePacket(String content) {
        super(content);
    }

    public String name() {
        return this.content();
    }
}
