package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "info", aliases = {"me"})
public class InfoCommand {

    @DefaultCommand
    public void handle() {
        CloudAPI.instance().logger().info("This are the information");
    }
}
