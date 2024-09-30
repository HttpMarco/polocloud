package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.osgan.networking.CommunicationProperty;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.cluster.NodeEndpoint;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.cluster.impl.ExternalNode;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import dev.httpmarco.polocloud.node.packets.ClusterAuthTokenPacket;
import dev.httpmarco.polocloud.node.packets.ClusterMergeFamilyPacket;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

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
        var portArgument = CommandArgumentType.Integer("port");
        var nodeToken = CommandArgumentType.Text("externalToken");

        syntax(commandContext -> {
            var name = commandContext.arg(nameArgument);
            var hostname = commandContext.arg(hostnameArgument);
            var port = commandContext.arg(portArgument);
            var token = commandContext.arg(nodeToken);

            // check if a node already exists
            if (clusterProvider.find(name) != null) {
                log.warn("A node with the name &b{}&8 already exists&8!", name);
                return;
            }

            var data = new NodeEndpointData(name, hostname, port);
            var endpoint = new ExternalNode(data);


            endpoint.connect(transmit -> {
                transmit.request("auth-cluster-token", new CommunicationProperty().set("token", token), ClusterAuthTokenPacket.class, response -> {
                    var result = response.value();

                    if(result) {
                        var clusterConfig = Node.instance().nodeConfig();
                        var nodes = new HashSet<>(clusterConfig.nodes());
                        nodes.add(Node.instance().nodeConfig().localNode());

                        transmit.sendPacket(new ClusterMergeFamilyPacket(clusterConfig.clusterId(), clusterConfig.clusterToken(),nodes));

                        // register the new endpoint and
                        clusterProvider.endpoints().add(endpoint);

                        // update global node configuration
                        Node.instance().nodeConfig().nodes().add(endpoint.data());
                        Node.instance().updateNodeConfig();

                        // todo test the token

                        log.info("Successfully registered &b{}&8!", name);
                        return;
                    }
                    // token is invalid -> channel is now closed!
                    log.warn("Failed to register &b{}&8! &7The provided token is invalid!", name);
                });
            }, transmit -> {
                // the connection is failed -> not save anything
                log.error("Failed to register &b{}&8! &7The required node endpoint is offline!", name);
            });
        }, "Register a new node in own cluster&8.", CommandArgumentType.Keyword("merge"), nameArgument, hostnameArgument, portArgument, nodeToken);
    }
}
