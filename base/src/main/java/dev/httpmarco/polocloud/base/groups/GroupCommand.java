package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;

public class GroupCommand {

    @Command(command = "groups")
    public void handle() {
        CloudAPI.instance().groupService().createGroup("lobby", 512, 5);
    }

}
