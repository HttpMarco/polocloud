package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", "Manage your modules", "mod");

        var moduleProvider = Node.instance().moduleProvider();

        var moduleIdArgument = CommandArgumentType.ModuleArgument("id");

        syntax(context -> {
            var module = context.arg(moduleIdArgument);
            if (module == null) {
                log.warn(moduleIdArgument.wrongReason());
                return;
            }

            var metadata = module.metadata();

            log.info("Module &8(&fID = &b{}&8)&f Information's:", metadata.id());
            log.info("&8- &fName&8: &7{}", metadata.name());
            log.info("&8- &fAuthor&8: &7{}", metadata.author());
            log.info("&8- &fDescription&8: &7{}", metadata.description());
            log.info("&8- &fMain Class&8: &7{}", metadata.main());

        }, "Displays all Information about an Module&8.", moduleIdArgument, CommandArgumentType.Keyword("info"));
    }
}
