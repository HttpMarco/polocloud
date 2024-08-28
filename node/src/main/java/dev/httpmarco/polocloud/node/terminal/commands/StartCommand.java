package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StartCommand extends Command {

    public StartCommand() {
        super("start", "Start a new count of services");

        var groupArgument = CommandArgumentType.ClusterGroup("group");

        syntax(commandContext -> {
            start(commandContext.arg(groupArgument), 1);
            log.info("Start one new service of group {}&8!", commandContext.arg(groupArgument).name());
        }, groupArgument);

        var amountIndex = CommandArgumentType.Integer("amount");
        syntax(commandContext -> {
            start(commandContext.arg(groupArgument), commandContext.arg(amountIndex));
            log.info("Start {} new services of group {}&8!", commandContext.arg(amountIndex), commandContext.arg(groupArgument).name());
        }, groupArgument, amountIndex);
    }

    public void start(ClusterGroup group, int amount) {
        for (int i = 0; i < amount; i++) {
            Node.instance().serviceProvider().factory().runGroupService(group);
        }
    }
}
