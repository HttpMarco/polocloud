package dev.httpmarco.polocloud.api.packet;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class ConnectionAuthPacket extends Packet {

    private String token;
    private String id;
    private Reason reason;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.token = packetBuffer.readString();
        this.id = packetBuffer.readString();
        this.reason = packetBuffer.readEnum(Reason.class);
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(token);
        packetBuffer.writeString(id);
        packetBuffer.writeEnum(reason);
    }

    public enum Reason {
        NODE, SERVICE
    }
}
