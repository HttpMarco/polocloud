package dev.httpmarco.polocloud.runner.services;

import dev.httpmarco.osgan.networking.codec.CodecBuffer;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.packets.CloudAllServicesPacket;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import dev.httpmarco.polocloud.runner.Instance;

import java.util.List;
import java.util.UUID;

public class InstanceServiceProvider implements CloudServiceProvider {
    @Override
    public CloudServiceFactory factory() {
        return null;
    }

    @Override
    public List<CloudService> services() {
        System.out.println("sending packet");
        // todo
        Instance.instance().client().transmitter().request("services-all", CloudAllServicesPacket.class, it -> {

            for (CloudService service : it.services()) {
                System.out.println(service.orderedId());
            }
        });
        return List.of();
    }

    @Override
    public List<CloudService> services(CloudGroup group) {
        return List.of();
    }

    @Override
    public CloudService find(UUID id) {
        return null;
    }

    @Override
    public CloudService service(String name) {
        return null;
    }

    @Override
    public CloudService fromPacket(CodecBuffer buffer) {
        return new InstanceCloudService(buffer.readInt());
    }
}
