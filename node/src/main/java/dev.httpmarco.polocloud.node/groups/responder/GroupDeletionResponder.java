package dev.httpmarco.polocloud.node.groups.responder;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.packet.MessageResponsePacket;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupDeletePacket;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class GroupDeletionResponder {

    public Packet handle(@NotNull ClusterGroupProvider groupService, ClusterProvider clusterProvider, @NotNull CommunicationProperty property) {
        var name = property.getString("name");

        if (!groupService.exists(name)) {
            return MessageResponsePacket.fail("The group does not exists!");
        }

        clusterProvider.broadcastAll(new GroupDeletePacket(name));

        return MessageResponsePacket.success();
    }
}
