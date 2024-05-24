package dev.httpmarco.polocloud.base.node;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;

@Command(command = "node", aliases = {"nodes"}, description = "Manage all your nodes or connect other ones.")
public final class NodeCommand {

    @DefaultCommand
    public void handle() {
        var logger = CloudAPI.instance().logger();
        logger.info("node list - list all nodes");
        logger.info("node add <hostname> <port> - node ");
        logger.info("node remove <name> - test description");
        logger.info("node status- test description");
    }

    @SubCommand(args = {"list"})
    public void list() {
        CloudAPI.instance().logger().info("polo");
    }
}