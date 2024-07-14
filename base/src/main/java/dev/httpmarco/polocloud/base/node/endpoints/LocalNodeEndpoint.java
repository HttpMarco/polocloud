package dev.httpmarco.polocloud.base.node.endpoints;

import dev.httpmarco.osgan.networking.server.CommunicationServer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceRegisterPacket;
import dev.httpmarco.polocloud.base.node.data.NodeData;
import dev.httpmarco.polocloud.base.node.NodeSituation;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class LocalNodeEndpoint extends NodeEndpoint {

    private final CommunicationServer server;

    public LocalNodeEndpoint(NodeData data) {
        super(data, NodeSituation.INITIALIZE);

        this.server = new CommunicationServer(data.hostname(), data.port());

        server.listen(CloudServiceRegisterPacket.class, (channelTransmit, cloudServiceRegisterPacket) -> {
            var service = CloudAPI.instance().serviceProvider().find(cloudServiceRegisterPacket.uuid());

            // first touch of services are the self started node endpoint
            // After this, other nodes synced this data
            if (service instanceof LocalCloudService localCloudService) {
                localCloudService.channelTransmit(channelTransmit);
            }
        });
        server.initialize();
    }
}
