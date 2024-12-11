package dev.httpmarco.polocloud.node.commands.impl;

import dev.httpmarco.polocloud.node.NodeShutdown;
import dev.httpmarco.polocloud.node.commands.Command;

public final class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", Node.translation().get("command.stop.description"), "stop", "exit");



        defaultExecution(commandContext -> NodeShutdown.nodeShutdownTotal(false));
    }
}
