package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import dev.httpmarco.polocloud.node.groups.ClusterGroupEditFields;
import dev.httpmarco.polocloud.node.terminal.setup.impl.GroupSetup;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class GroupCommand extends Command {

    public GroupCommand() {
        super("group", "Manage or create your cluster groups", "groups");

        var groupService = Node.instance().groupProvider();

        // argument for group name
        var groupArgument = CommandArgumentType.ClusterGroup("group");

        syntax(context -> {
            log.info("Following &b{} &7groups are loaded&8:", groupService.groups().size());
            groupService.groups().forEach(group -> log.info("&8- &f{}&8: (&7{}&8)", group.name(), group.details()));
        }, "List all registered groups&8.", CommandArgumentType.Keyword("list"));

        syntax(context -> new GroupSetup().run(), "Create a new cluster group.", CommandArgumentType.Keyword("create"));

        syntax(context -> groupService.delete(context.arg(groupArgument).name())
                        .ifPresentOrElse(
                                s -> log.warn("Cannot delete group: {}", s),
                                () -> log.info("Successfully delete group {} in cluster!", context.arg(groupArgument).name())
                        ), "Delete the selected group&8.", groupArgument, CommandArgumentType.Keyword("delete"));

        syntax(context -> {
            var group = context.arg(groupArgument);
            log.info("Name&8: &b{}", group.name());
            log.info("Runtime nodes&8: &b{}", String.join("&8, &b", group.nodes()));
            log.info("Platform&8: &b{}", group.platform().details());
            log.info("Static service&8: &b{}", group.staticService());
            log.info("Maximum memory&8: &b{}mb", group.maxMemory());
            log.info("Minimum online services&8: &b{}", group.minOnlineServerInstances());
            log.info("Maximum online services&8: &b{}", group.maxOnlineServerInstances());
        }, "Show all information about a group&8.", groupArgument, CommandArgumentType.Keyword("info"));

        syntax(context -> {
            var group = context.arg(groupArgument);
            if (group.services().isEmpty()) {
                log.info("This group has no services&8!");
                return;
            }

            log.info("Stopping all services of the group &8'&f{}&8'...", group.name());
            for (ClusterService service : group.services()) {
                service.shutdown();
            }

        }, "Shutdown all services with this group&8.", groupArgument, CommandArgumentType.Keyword("shutdown"));


        var editKey = CommandArgumentType.Enum(ClusterGroupEditFields.class, "key");
        var editValue = CommandArgumentType.Text("value");

        syntax(context -> {
            var group = context.arg(groupArgument);
            var editableField = context.arg(editKey);
            var value = context.arg(editValue);

            log.info("{}:{}: {}", group.name(), editableField.name(), value);

        }, "Change a property of a group&8.", groupArgument, CommandArgumentType.Keyword("edit"), editKey, editValue);

    }
}
