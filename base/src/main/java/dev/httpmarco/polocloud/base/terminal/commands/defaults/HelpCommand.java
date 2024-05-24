package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "help", aliases = {"?"}, description = "Help you with all commands")
public final class HelpCommand {

    @DefaultCommand
    public void handle() {
        for (var command : CloudBase.instance().terminal().commandService().commands()) {

            var commandInfo = command.getClass().getDeclaredAnnotation(Command.class);

            var aliases = "";

            if (commandInfo.aliases().length != 0) {
                aliases = " &2(&1" + String.join("&2, &1", commandInfo.aliases()) + "&2)";
            }

            CloudAPI.instance().logger().info("&3" + commandInfo.command() + aliases +" &2- &1" + commandInfo.description() + "&2.");
        }
    }
}
