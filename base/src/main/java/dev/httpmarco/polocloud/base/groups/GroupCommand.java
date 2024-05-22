package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

public class GroupCommand {

    @Command(command = "groups")
    public void handle() {
        // send todo
        //  CloudAPI.instance().groupProvider().createGroup("lobby", "paper-1.20.6", 512, 5);
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        var logger = CloudAPI.instance().logger();
        var groups = CloudAPI.instance().groupProvider().groups();

        logger.info("Following &3" + groups.size() + " &1groups are loading&2:");

        groups.forEach(cloudGroup -> {
            logger.info("&2- &4" + cloudGroup.name() + "&2: (&1" + cloudGroup.toString() + "&2)");
        });
    }

    @SubCommand(args = {"create", "<name>"})
    public void handleCreate(String name) {
        if (CloudAPI.instance().groupProvider().createGroup(name, "paper-1.20.6", 512, 1)) {
            CloudAPI.instance().logger().info("fucking yheay");
        }
    }
}