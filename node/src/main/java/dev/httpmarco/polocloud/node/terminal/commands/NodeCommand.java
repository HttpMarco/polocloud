package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;

public class NodeCommand extends Command {

    public NodeCommand() {
        super("node", "Manage your node");

        syntax(context -> Node.instance().autoUpdater().update(), "Update your current Node&8.", CommandArgumentType.Keyword("update"));
    }
}