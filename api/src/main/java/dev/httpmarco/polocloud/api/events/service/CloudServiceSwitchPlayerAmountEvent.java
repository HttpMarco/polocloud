package dev.httpmarco.polocloud.api.events.service;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudServiceSwitchPlayerAmountEvent implements ServiceEvent{
    private final CloudService cloudService;
    private final List<CloudPlayer> cloudPlayers;

    public Integer getAmount(){
        return cloudPlayers.size();
    }

    @Override
    public void read(CodecBuffer buffer) {

    }

    @Override
    public void write(CodecBuffer buffer) {

    }
}
