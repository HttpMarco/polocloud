package dev.httpmarco.polocloud.suite.groups.commands;

import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.GroupArgument;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.groups.setup.GroupSetup;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class GroupCommand extends Command {

    public GroupCommand(ClusterGroupProvider groupProvider) {
        super("group", "Manage all your cluster groups", "groups");

        var translation = PolocloudSuite.instance().translation();

        syntax(commandContext -> {

            if (groupProvider.findAll().isEmpty()) {
                log.info(translation.get("suite.command.group.list.noGroups"));
                return;
            }

            log.info(translation.get("suite.command.group.list.header"));
            groupProvider.findAll().forEach(clusterGroup -> log.info(" - {}", clusterGroup.name()));

        }, new KeywordArgument("list"));

        var groupArgument = new GroupArgument("group");
        syntax(commandContext -> {
            var group = commandContext.arg(groupArgument);

            if(group == null) {
                log.info(translation.get("suite.command.group.info.notFound"));
                return;
            }

            log.info(translation.get("suite.command.group.info.detailsHeader", group.name()));
            log.info(translation.get("suite.command.group.info.platformHeader"));
            log.info(translation.get("command.group.info.platformType", group.platform().type()));
            log.info(translation.get("command.group.info.platformVersion", group.platform().version()));
            log.info(translation.get("suite.command.group.info.minMemory", group.minMemory()));
            log.info(translation.get("suite.command.group.info.maxMemory", group.maxMemory()));
            log.info(translation.get("suite.command.group.info.minOnline", group.minOnlineService()));
            log.info(translation.get("suite.command.group.info.maxOnline", group.maxOnlineService()));
            log.info(translation.get("suite.command.group.info.percentageToStartNewService", group.percentageToStartNewService()));
            log.info(translation.get("suite.command.group.info.runningServices", group.runningServicesAmount()));
        }, new KeywordArgument("info"), groupArgument);


        syntax(commandContext -> new GroupSetup().run(), "Create a new group", new KeywordArgument("create"));
    }
}
