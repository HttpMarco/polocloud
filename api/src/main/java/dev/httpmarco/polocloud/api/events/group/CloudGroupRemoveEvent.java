package dev.httpmarco.polocloud.api.events.group;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CloudGroupRemoveEvent implements GroupEvent {
    private final CloudGroup cloudGroup;

    @Override
    public void read(CodecBuffer buffer) {

    }

    @Override
    public void write(CodecBuffer buffer) {

    }
}
