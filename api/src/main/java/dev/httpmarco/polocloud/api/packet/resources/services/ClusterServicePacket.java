package dev.httpmarco.polocloud.api.packet.resources.services;

import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class ClusterServicePacket extends Packet {

    private boolean nullState;
    private ClusterService service;

    @Override
    public void read(@NotNull PacketBuffer packetBuffer) {
        this.nullState = packetBuffer.readBoolean();

        if(!nullState){
            this.service = CloudAPI.instance().serviceProvider().read(packetBuffer);
        }
    }

    @Override
    public void write(@NotNull PacketBuffer packetBuffer) {
        packetBuffer.writeBoolean(service == null);

        if(service != null) {
            CloudAPI.instance().serviceProvider().write(service, packetBuffer);
        }
    }
}
