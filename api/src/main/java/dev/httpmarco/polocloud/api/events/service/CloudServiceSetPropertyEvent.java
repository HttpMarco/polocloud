package dev.httpmarco.polocloud.api.events.service;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudServiceSetPropertyEvent implements ServiceEvent{
    private final CloudService cloudService;

    @Override
    public void read(CodecBuffer buffer) {

    }

    @Override
    public void write(CodecBuffer buffer) {

    }

    //todo: Add changed property
}
