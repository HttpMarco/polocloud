package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "reload", aliases = {"rl"}, description = "Reload all configurations and services")
public final class ReloadCommand {

    @DefaultCommand
    public void handle() {
        // todo
    }

}
