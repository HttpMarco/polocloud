package dev.httpmarco.polocloud.node.groups.responder;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.osgan.networking.packet.Packet;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.packet.resources.group.GroupCreatePacket;
import dev.httpmarco.polocloud.api.packet.MessageResponsePacket;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.util.JsonUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@UtilityClass
public class GroupCreationResponder {

    public Packet handle(@NotNull ClusterGroupProvider groupService, ClusterProvider clusterProvider, @NotNull CommunicationProperty property) {

        var name = property.getString("name");

        if (groupService.exists(name)) {
            return MessageResponsePacket.fail("Group already exists!");
        }

        var maxMemory = property.getInteger("maxMemory");

        if (maxMemory < 1) {
            return MessageResponsePacket.fail("The max memory must be higher than 1 mb.");
        }


        var nodes = JsonUtils.GSON.fromJson(property.getString("nodes"), String[].class);
        var platform = Node.instance().platformService().find(property.getString("platform"));
        var groupDisplay = new PlatformGroupDisplay(platform.id(), property.getString("version"), platform.type());

        // alert on every node the new group
        clusterProvider.broadcastAll(new GroupCreatePacket(
                name,
                new String[]{name, "every", groupDisplay.type().defaultTemplateSpace()},
                nodes,
                groupDisplay,
                maxMemory,
                property.getBoolean("staticService"),
                property.getInteger("minOnline"),
                property.getInteger("maxOnline"),
                property.has("fallback") && property.getBoolean("fallback"))
        );

        return MessageResponsePacket.success();
    }
}
