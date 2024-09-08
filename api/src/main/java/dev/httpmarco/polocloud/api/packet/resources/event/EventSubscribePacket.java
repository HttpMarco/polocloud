package dev.httpmarco.polocloud.api.packet.resources.event;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.event.Event;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public final class EventSubscribePacket extends Packet {

    private String packetClass;

    public EventSubscribePacket(@NotNull Class<? extends Event> event) {
        this.packetClass = event.getName();
    }

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.packetClass = packetBuffer.readString();
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeString(packetClass);
    }
}
