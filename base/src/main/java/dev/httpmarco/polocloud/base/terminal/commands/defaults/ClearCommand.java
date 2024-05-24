package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "clear", aliases = {"cc"}, description = "Clear the current console output in your screen")
public final class ClearCommand {

    @DefaultCommand
    public void handle() {
        CloudBase.instance().terminal().clear();
    }

}
