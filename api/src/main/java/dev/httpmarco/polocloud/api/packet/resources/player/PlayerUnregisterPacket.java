package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.polocloud.api.packet.AbstractUUIDPacket;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PlayerUnregisterPacket extends AbstractUUIDPacket {

    public PlayerUnregisterPacket(UUID uuid) {
        super(uuid);
    }
}