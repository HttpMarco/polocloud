package dev.httpmarco.polocloud.suite.groups.commands;

import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.GroupArgument;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class GroupCommand extends Command {

    public GroupCommand(ClusterGroupProvider groupProvider) {
        super("group", "Manage all your cluster groups", "groups");

        syntax(commandContext -> {

            if (groupProvider.findAll().isEmpty()) {
                log.info("The cluster has no groups yet.");
                return;
            }

            log.info("The cluster has the following groups:");
            groupProvider.findAll().forEach(clusterGroup -> log.info(" - {}", clusterGroup.name()));

        }, new KeywordArgument("list"));

        var groupArgument = new GroupArgument("group");
        syntax(commandContext -> {
            var group = commandContext.arg(groupArgument);

            if(group == null) {
                log.info("The group does not exist.");
                return;
            }

            log.info("All details about the group &f{}&8:", group.name());
            log.info("&8- &7Platform&8:");
            log.info("  &8- &7Type&8: &f{}", group.platform().type());
            log.info("  &8- &7Version&8: &f{}", group.platform().version());
            log.info("&8- &7min Memory&8: &f{}", group.minMemory());
            log.info("&8- &7max Memory&8: &f{}", group.maxMemory());

        }, new KeywordArgument("info"), groupArgument);
    }
}
