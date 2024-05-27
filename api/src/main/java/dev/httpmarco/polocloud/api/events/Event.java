package dev.httpmarco.polocloud.api.events;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;

public interface Event {

    void read(CodecBuffer buffer);

    void write(CodecBuffer buffer);

}
