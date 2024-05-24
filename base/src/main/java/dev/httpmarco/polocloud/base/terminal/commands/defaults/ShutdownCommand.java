package dev.httpmarco.polocloud.base.terminal.commands.defaults;

import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "shutdown", aliases = {"exit", "stop"}, description = "Shutdown the cloud and all nodes services")
public final class ShutdownCommand {

    @DefaultCommand
    public void handle() {
        CloudBase.instance().shutdown(false);
    }
}
