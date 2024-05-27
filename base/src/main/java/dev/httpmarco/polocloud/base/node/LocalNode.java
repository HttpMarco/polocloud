package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.files.annotations.ConfigExclude;
import dev.httpmarco.osgan.networking.server.NettyServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.node.AbstractNode;
import dev.httpmarco.polocloud.api.packets.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class LocalNode extends AbstractNode implements dev.httpmarco.polocloud.api.node.LocalNode {

    @ConfigExclude
    private NettyServer server;

    public LocalNode(UUID id, String name, String hostname, int port) {
        super(id, name, hostname, port);
    }

    public void initialize() {
        server = NettyServer.builder().onInactive(channelTransmit -> {
            // todo
        }).build();

        server.listen(CloudServiceRegisterPacket.class, (channelTransmit, cloudServiceRegisterPacket) -> {
            var service = CloudAPI.instance().serviceProvider().find(cloudServiceRegisterPacket.uuid());

            if (service instanceof LocalCloudService localCloudService) {
                localCloudService.channelTransmit(channelTransmit);
                CloudAPI.instance().logger().info("Server " + service.name() + " is now successfully started up&2.");
            } else {
                //todo
            }
        });
    }

    @Override
    public void close() {
        if (this.server != null) {
            this.server.close();
        }
    }
}
