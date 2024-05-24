package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;


@Command(command = "groups", aliases = {"group"}, description = "Manage or create your cluster groups")
public final class GroupCommand {

    @DefaultCommand
    public void handle() {
        CloudAPI.instance().logger().info("&3groups create &2<&1name&2> &2<&1platform&2> &2<&1memory&2> &2<&1minOnlineCount&2> &2- &1Create a new group&2.");
        CloudAPI.instance().logger().info("&3groups delete &2<&1name&2> &2- &1Delete a existing group&2.");
        CloudAPI.instance().logger().info("&3groups edit &2<&1name&2> &2<&1key&2> &2<&1value&2> &2- &1Edit a value in a group&2.");
        CloudAPI.instance().logger().info("&3groups shutdown &2<&1name&2> &2- &1Shutdown all services of a group&2.");
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        var logger = CloudAPI.instance().logger();
        var groups = CloudAPI.instance().groupProvider().groups();

        logger.info("Following &3" + groups.size() + " &1groups are loading&2:");

        groups.forEach(cloudGroup -> logger.info("&2- &4" + cloudGroup.name() + "&2: (&1" + cloudGroup + "&2)"));
    }

    @SubCommand(args = {"create", "<name>", "<platform>", "<memory>", "<minOnlineCount>"})
    public void handleCreate(String name, String platform, int memory, int minOnlineCount) {
        if (CloudAPI.instance().groupProvider().createGroup(name, platform, memory, minOnlineCount)) {
            CloudAPI.instance().logger().info("Successfully created &3" + name + " &1group&2.");
        }
    }

    @SubCommand(args = {"delete", "<name>"})
    public void handleCreate(String name) {
        if (CloudAPI.instance().groupProvider().deleteGroup(name)) {
            CloudAPI.instance().logger().info("Successfully deleted &3" + name + "&2!");
        }
    }

    @SubCommand(args = {"edit", "<name>", "<key>", "<value>"})
    public void handleEdit(String name, String key, String value) {
        //todo
    }

    @SubCommand(args = {"shutdown", "<name>"})
    public void handleShutdown(String name) {
        var group = CloudAPI.instance().groupProvider().group(name);
        var logger = CloudAPI.instance().logger();

        if(group == null) {
            logger.info("The group does not exists&2!");
            return;
        }
        CloudAPI.instance().serviceProvider().services(group).forEach(CloudService::shutdown);
        logger.info("You successfully stopped all services of group &3" + name + "&2!");
    }
}