package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ServiceCommand extends Command {

    public ServiceCommand() {
        super("service", "Manage all your services", "services", "ser");

        var serviceArgument = CommandArgumentType.ClusterService("service");

        syntax(it -> {
            var serviceProvider = Node.instance().serviceProvider();

            log.info("Following &b{} &7groups are loading&8:", serviceProvider.services().size());
            serviceProvider.services().forEach(group -> log.info("&8- &f{}&8: (&7{}&8)", group.name(), group.details()));
        }, CommandArgumentType.Keyword("list"));

        syntax(it -> {
            var service = it.arg(serviceArgument);
            log.info("Name&8: &b{}", service.name());
            log.info("Running node&8: &b{}", String.join("&8, &b", service.runningNode()));
            log.info("Group&8: &b{}", service.group().name());
            log.info("Bind&8: &b{}&8:&b{}", service.hostname(), service.port());
            log.info("State&8: &b{}", service.state());
            log.info("Maximal players: &b{}" , service.maxPlayers());
            log.info("Online players&8: &b{}", service.onlinePlayersCount());
            log.info("Properties&8(&b{}&8):", service.properties().properties().size());
            service.properties().properties().forEach((property, o) -> log.info("&8 - &7{}&8 = &7{}", property.name(), o));
        }, serviceArgument, CommandArgumentType.Keyword("info"));


        syntax(it -> {
            var service = it.arg(serviceArgument);
            log.info("Trying to shutdown &8'&f{}'&8", service.name());

            // todo
            service.shutdown();
        }, serviceArgument, CommandArgumentType.Keyword("shutdown"));


        syntax(it -> {
            var service = it.arg(serviceArgument);
            Node.instance().terminal().clear();
            log.info("Logs of service {}&8: ", service.name());

            for (var logLine : service.logs()) {
                log.info(logLine);
            }
        }, serviceArgument, CommandArgumentType.Keyword("log"));

        syntax(it -> Node.instance().screenProvider().display(it.arg(serviceArgument)), serviceArgument, CommandArgumentType.Keyword("screen"));

        var commandArg = CommandArgumentType.StringArray("command");
        syntax(it -> {
            var service = it.arg(serviceArgument);
            var command = it.arg(commandArg);

            service.executeCommand(command);
            log.info("&b{} &8-> &b{} &8| &7{}", Node.instance().clusterProvider().localNode().data().name(), service.name(), command);
        }, serviceArgument, CommandArgumentType.Keyword("execute"), commandArg);
    }
}
