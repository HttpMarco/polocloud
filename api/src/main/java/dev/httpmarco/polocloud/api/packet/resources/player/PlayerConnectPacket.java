package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.AbstractUUIDPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PlayerConnectPacket extends AbstractUUIDPacket {

    private String serverId;

    public PlayerConnectPacket(UUID uuid, String serverId) {
        super(uuid);
        this.serverId = serverId;
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        this.serverId = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        packetBuffer.writeString(serverId);
    }
}
