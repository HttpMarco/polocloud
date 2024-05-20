package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;

public class GroupCommand {

    @Command(command = "groups")
    public void handle() {
        CloudAPI.instance().groupProvider().createGroup("lobby", "paper-1.20.6", 512, 5);
    }
}
