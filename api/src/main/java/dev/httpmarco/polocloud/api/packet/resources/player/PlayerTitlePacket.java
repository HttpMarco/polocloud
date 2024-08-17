package dev.httpmarco.polocloud.api.packet.resources.player;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.packet.AbstractUUIDPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class PlayerTitlePacket extends AbstractUUIDPacket {

    private String title;
    private String subTitle;

    private int fadeIn;
    private int stay;
    private int fadeOut;

    public PlayerTitlePacket(UUID uuid, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        super(uuid);
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        this.title = packetBuffer.readString();
        this.subTitle = packetBuffer.readString();
        this.fadeIn = packetBuffer.readInt();
        this.stay = packetBuffer.readInt();
        this.fadeOut = packetBuffer.readInt();

    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        packetBuffer.writeString(title);
        packetBuffer.writeString(subTitle);
        packetBuffer.writeInt(fadeIn);
        packetBuffer.writeInt(stay);
        packetBuffer.writeInt(fadeOut);
    }
}
