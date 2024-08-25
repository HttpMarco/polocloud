package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PlatformCommand extends Command {

    public PlatformCommand() {
        super("platform", "Manage all your local cluster platforms", "platforms");

        syntax(commandContext -> {
            var platformService = Node.instance().platformService();

            log.info("Following &b{} &7platforms are loaded&8:", platformService.platforms().size());
            platformService.platforms().forEach(platform -> log.info("&8- &f{}&8: (&7{}&8)", platform.id(), platform.details()));
        }, CommandArgumentType.Keyword("list"));


        // todo add command
        // todo remove command
    }
}
