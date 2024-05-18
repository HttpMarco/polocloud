package dev.httpmarco.polocloud.base.terminal.commands;

import dev.httpmarco.polocloud.base.CloudBase;

public final class ShutdownCommand {

    @Command(command = "shutdown", aliases = {"exit", "stop"})
    public void handle() {
        CloudBase.instance().shutdown();
    }
}
