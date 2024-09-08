package dev.httpmarco.polocloud.api.packet.resources.services;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class ServiceCommandPacket extends AbstractClusterServiceIdPacket {

    private String command;

    public ServiceCommandPacket(UUID id, String command) {
        super(id);
        this.command = command;
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        super.write(packetBuffer);
        packetBuffer.writeString(command);
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        super.read(packetBuffer);
        this.command = packetBuffer.readString();
    }
}
