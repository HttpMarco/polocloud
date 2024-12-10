package dev.httpmarco.polocloud.node.terminal.commands.impl;

import dev.httpmarco.polocloud.node.NodeShutdown;
import dev.httpmarco.polocloud.node.terminal.commands.Command;

public final class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", "Shutdown the cloud and all node services", "stop", "exit");



        defaultExecution(commandContext -> NodeShutdown.nodeShutdownTotal(false));
    }
}