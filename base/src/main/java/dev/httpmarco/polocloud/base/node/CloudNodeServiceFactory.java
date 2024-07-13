package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.client.CommunicationClient;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.packets.nodes.NodeBindPacket;
import dev.httpmarco.polocloud.base.CloudBase;
import org.jetbrains.annotations.NotNull;

public final class CloudNodeServiceFactory {

    public void bind(@NotNull ExternalNode node) {
        new CommunicationClient(node.hostname(), node.port(), channelTransmit -> {
            var localNode = CloudAPI.instance().nodeService().localNode();
            channelTransmit.request("node-verify", new CommunicationProperty()
                            .set("name", localNode.name())
                            .set("id", localNode.id())
                            .set("hostname", localNode.hostname())
                            .set("port", localNode.port())
                            .set("self-name", node.name())
                            .set("self-id", node.id()),

                    NodeBindPacket.class, nodeBindPacket -> {
                        if (!nodeBindPacket.successfully()) {
                            CloudAPI.instance().logger().warn(nodeBindPacket.reason());
                        } else {
                            CloudBase.instance().nodeService().externalNodes().add(node);
                            CloudBase.instance().cloudConfiguration().content().externalNodes().add(node);
                            CloudBase.instance().cloudConfiguration().save();

                            CloudBase.instance().logger().success("Successfully register " + node.name() + "&2.");
                        }
                    });
        }).initialize();
    }
}
