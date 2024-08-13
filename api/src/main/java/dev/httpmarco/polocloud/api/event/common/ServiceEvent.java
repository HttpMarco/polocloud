package dev.httpmarco.polocloud.api.event.common;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class ServiceEvent implements Event {

    private ClusterService service;

    @Override
    public void write(PacketBuffer buffer) {
        CloudAPI.instance().serviceProvider().write(service, buffer);
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.service = CloudAPI.instance().serviceProvider().read(buffer);
    }
}
