package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.AbstractUUIDPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PlayerRegisterPacket extends AbstractUUIDPacket {

    private String username;
    private UUID proxy;

    public PlayerRegisterPacket(UUID uuid, String username, UUID proxy) {
        super(uuid);
        this.username = username;
        this.proxy = proxy;
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        super.read(packetBuffer);

        this.username = packetBuffer.readString();
        this.proxy = packetBuffer.readUniqueId();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        super.write(packetBuffer);

        packetBuffer.writeString(username);
        packetBuffer.writeUniqueId(proxy);
    }
}
