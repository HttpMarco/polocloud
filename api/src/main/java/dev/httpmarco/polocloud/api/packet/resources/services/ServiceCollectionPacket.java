package dev.httpmarco.polocloud.api.packet.resources.services;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ServiceCollectionPacket extends Packet {

    private List<ClusterService> services;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        var amount = packetBuffer.readInt();
        this.services = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            this.services.add(CloudAPI.instance().serviceProvider().read(packetBuffer));
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeInt(services.size());

        for (ClusterService service : services) {
            CloudAPI.instance().serviceProvider().write(service, packetBuffer);
        }
    }
}
