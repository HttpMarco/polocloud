package dev.httpmarco.polocloud;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;

public interface Sendable<T> {

    void write(T value, PacketBuffer buffer);

    T read(PacketBuffer buffer);

}
