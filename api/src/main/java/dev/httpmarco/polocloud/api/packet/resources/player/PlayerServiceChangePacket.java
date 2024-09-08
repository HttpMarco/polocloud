package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.AbstractUUIDPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PlayerServiceChangePacket extends AbstractUUIDPacket {

    private String service;

    public PlayerServiceChangePacket(UUID uuid, String service) {
        super(uuid);
        this.service = service;
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        this.service = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        packetBuffer.writeString(service);
    }
}
