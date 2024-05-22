package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

public class GroupCommand {

    @Command(command = "groups")
    public void handle() {
        CloudAPI.instance().groupProvider().createGroup("lobby", "paper-1.20.6", 512, 5);
    }

    @SubCommand(args = {"list"})
    public void handleList() {
        CloudAPI.instance().groupProvider().groups().forEach(cloudGroup -> {
            CloudAPI.instance().logger().info(cloudGroup.name());
        });
    }
}
