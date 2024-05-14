package dev.httpmarco.polocloud.base.terminal.commands.impl;

import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.terminal.commands.Command;

public final class ShutdownCommand {

    @Command(command = "shutdown", aliases = {"exit", "stop"})
    public void handle() {
        CloudBase.instance().shutdown();
    }
}
