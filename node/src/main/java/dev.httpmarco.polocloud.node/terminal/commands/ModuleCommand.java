package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", "Manage your modules", "mod");

        var moduleIdArgument = CommandArgumentType.Text("id"); //TODO add predication

        syntax(context -> {
            var loadedModules = Node.instance().moduleProvider().getLoadedModules();
            var moduleId = context.arg(moduleIdArgument);
            var module = loadedModules.stream().filter(it -> it.metadata().id().equals(moduleId)).findFirst();

            if (module.isEmpty()) {
                log.info("Module not found&8!");
                return;
            }

            var metadata = module.get().metadata();

            log.info("Id: {}", metadata.id());
            log.info("Name: {}", metadata.name());
            log.info("Author: {}", metadata.author());
            log.info("Description: {}", metadata.description());
            log.info(" ");
            log.info("Main: {}", metadata.main());

        }, "Displays all Information about an Module&8.", moduleIdArgument, CommandArgumentType.Keyword("info"));
    }
}
