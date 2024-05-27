package dev.httpmarco.polocloud.api.events.player;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CloudPlayerSwitchServerEvent implements CloudPlayerEvent {
    private final CloudPlayer cloudPlayer;
    private final CloudService cloudService;

    @Override
    public void read(CodecBuffer buffer) {

    }

    @Override
    public void write(CodecBuffer buffer) {

    }
}
