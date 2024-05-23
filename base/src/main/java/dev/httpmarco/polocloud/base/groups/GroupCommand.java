package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

public final class GroupCommand {

    @Command(command = "groups")
    public void handle() {
        // send todo
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        var logger = CloudAPI.instance().logger();
        var groups = CloudAPI.instance().groupProvider().groups();

        logger.info("Following &3" + groups.size() + " &1groups are loading&2:");

        groups.forEach(cloudGroup -> {
            logger.info("&2- &4" + cloudGroup.name() + "&2: (&1" + cloudGroup + "&2)");
        });
    }

    @SubCommand(args = {"create", "<name>", "<platform>", "<memory>", "<minOnlineCount>"})
    public void handleCreate(String name, String platform, int memory, int minOnlineCount) {
        if (CloudAPI.instance().groupProvider().createGroup(name, platform, memory, minOnlineCount)) {
            CloudAPI.instance().logger().info("Successfully created &3" + name + " &2group.");
        }
    }

    @SubCommand(args = {"delete", "<name>"})
    public void handleCreate(String name) {
        if (CloudAPI.instance().groupProvider().deleteGroup(name)) {
            CloudAPI.instance().logger().info("Successfully deleted " + name + "&2!");
        }
    }
}