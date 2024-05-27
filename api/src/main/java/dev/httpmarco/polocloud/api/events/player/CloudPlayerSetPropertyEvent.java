package dev.httpmarco.polocloud.api.events.player;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CloudPlayerSetPropertyEvent implements CloudPlayerEvent {
    private final CloudPlayer cloudPlayer;

    @Override
    public void read(CodecBuffer buffer) {

    }

    @Override
    public void write(CodecBuffer buffer) {

    }

    //todo: Add changed property
}
