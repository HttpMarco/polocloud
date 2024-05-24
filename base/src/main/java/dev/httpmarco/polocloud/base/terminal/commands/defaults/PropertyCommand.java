package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "properties", aliases = {"prop", "property"}, description = "Manage, set or remove global cluster properties")
public final class PropertyCommand {

    @DefaultCommand
    public void handle() {
        CloudAPI.instance().logger().info("Global properties");
        //todo
    }

}
