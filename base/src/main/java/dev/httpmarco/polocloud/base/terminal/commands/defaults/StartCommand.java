package dev.httpmarco.polocloud.base.terminal.commands.defaults;


import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.logging.Logger;
import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommand;
import dev.httpmarco.polocloud.base.terminal.commands.SubCommandCompleter;
import org.jline.reader.Candidate;

import java.util.List;

@Command(command = "start", aliases = "create", description = "Start a new service with a group and a count")
public class StartCommand {

    private final Logger logger = CloudAPI.instance().logger();

    @DefaultCommand
    public void handle() {
        logger.info("start &2<&1name&2> &2- &1Start a new service of a Group.&2.");
        logger.info("start &2<&1name&2> <&1count&2> &2- &1Start multiple services of a Group.&2.");
    }

    @SubCommand(args = "<name>")
    public void handleStart(String name) {
        if (!CloudAPI.instance().groupProvider().isGroup(name)) {
            logger.info("This group does not exists&2!");
            return;
        }

        var group = CloudAPI.instance().groupProvider().group(name);
        CloudAPI.instance().serviceProvider().factory().start(group);
        logger.info("Starting &31 &1new service of the group &2'&4" + group.name() + "&2'");
    }

    @SubCommand(args = {"<name>", "<count>"})
    public void handleStartWithCount(String name, int count) {
        if (!CloudAPI.instance().groupProvider().isGroup(name)) {
            logger.info("This group does not exists&2!");
            return;
        }

        if (count <= 0) {
            logger.warn("The count must be higher than 0");
            return;
        }

        var group = CloudAPI.instance().groupProvider().group(name);

        for (int i = 0; i < count; i++) {
            CloudAPI.instance().serviceProvider().factory().start(group);
        }

        logger.info("Starting &3" + count + "&1 new service of the group &2'&4" + group.name() + "&2'");
    }

    @SubCommandCompleter(completionPattern = {"<name>"})
    public void completeInfoMethod(int index, List<Candidate> candidates) {
        if (index == 1) {
            candidates.addAll(CloudAPI.instance().groupProvider().groups().stream().map(it -> new Candidate(it.name())).toList());
        }
    }
}
