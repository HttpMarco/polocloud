package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.impl.AbstractNode;
import dev.httpmarco.polocloud.node.cluster.impl.ExternalNode;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public final class ClusterCommand extends Command {

    public ClusterCommand(@NotNull ClusterProvider clusterProvider) {
        super("cluster", "Manager your cluster");

        var endpoints = clusterProvider.endpoints();
        var nodeArgument = CommandArgumentType.NodeEndpoint("node");

        syntax(context -> {
            if (endpoints.isEmpty()) {
                log.info("There are no endpoints registered&8!");
                return;
            }

            log.info("Following &f{} &7endpoints are registered&8:", endpoints.size());
            for (NodeEndpoint endpoint : endpoints) {
                log.info("&8- &f{}&8: (&7{}&8)", endpoint.data().name(), endpoint.data().details());
            }
        }, "List all Node endpoints&8.", CommandArgumentType.Keyword("list"));

        syntax(context -> {
            var node = context.arg(nodeArgument);
            log.info("Name&8: &b{}", node.data().name());
            log.info("Hostname&8: &b{}", node.data().hostname());
            log.info("Port&8: &b{}", node.data().port());
            log.info("Situation&8: &b{}", node.situation());
        }, "Show all information about a node&8.", nodeArgument, CommandArgumentType.Keyword("info"));

        var nameArgument = CommandArgumentType.Text("name");
        var hostnameArgument = CommandArgumentType.Text("hostname");
        var portArgument = CommandArgumentType.Integer("hostname");
        var nodeToken = CommandArgumentType.Text("token");

        syntax(commandContext -> {
            var name = commandContext.arg(nameArgument);
            var hostname = commandContext.arg(hostnameArgument);
            var port = commandContext.arg(portArgument);
            var token = commandContext.arg(portArgument);

            // check if a node already exists
            if(clusterProvider.find(name) != null) {
                log.warn("A node with the name &b{}&8 already exists&8!", name);
                return;
            }

            var data = new NodeEndpointData(name, hostname, port);
            var endpoint = new ExternalNode(data);

            // register the new endpoint and
            clusterProvider.endpoints().add(endpoint);
            // update global node configuration
            Node.instance().nodeConfig().nodes().add(endpoint.data());
            Node.instance().updateNodeConfig();

            // todo test connection

            log.info("Successfully registered &b{}&8!", name);
        }, "Register a new node in own cluster&8.", CommandArgumentType.Keyword("merge"), nameArgument, hostnameArgument, portArgument);
    }
}
