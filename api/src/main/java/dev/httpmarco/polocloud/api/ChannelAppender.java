package dev.httpmarco.polocloud.api;

import dev.httpmarco.osgan.networking.packet.Packet;

public interface ChannelAppender {

    void sendPacket(Packet packet);

}
