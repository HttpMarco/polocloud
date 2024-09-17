package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.AbstractUUIDPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PlayerTablistPacket extends AbstractUUIDPacket {

    private String header;
    private String footer;

    public PlayerTablistPacket(UUID uuid, String header, String footer) {
        super(uuid);
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        this.header = packetBuffer.readString();
        this.footer = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        packetBuffer.writeString(header);
        packetBuffer.writeString(footer);
    }
}
