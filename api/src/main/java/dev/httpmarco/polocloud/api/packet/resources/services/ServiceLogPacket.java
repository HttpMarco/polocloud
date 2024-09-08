package dev.httpmarco.polocloud.api.packet.resources.services;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Getter
public final class ServiceLogPacket extends Packet {

    private List<String> logs;

    public ServiceLogPacket(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(logs.size());

        for (var log : logs) {
            packetBuffer.writeString(log);
        }
    }


    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.logs = new ArrayList<>();
        var size = packetBuffer.readInt();

        for (int i = 0; i < size; i++) {
            logs.add(packetBuffer.readString());
        }
    }
}
