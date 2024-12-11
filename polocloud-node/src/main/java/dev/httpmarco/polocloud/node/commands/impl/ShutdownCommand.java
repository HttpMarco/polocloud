package dev.httpmarco.polocloud.node.commands.impl;

import dev.httpmarco.polocloud.node.NodeShutdown;
import dev.httpmarco.polocloud.node.commands.Command;

public final class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super(Node.translation().get("command.stop.name"), Node.translation().get("command.stop.description"), Node.translation().get("command.stop.alias.first"), Node.translation().get("command.stop.alias.second"));



        defaultExecution(commandContext -> NodeShutdown.nodeShutdownTotal(false));
    }
}
