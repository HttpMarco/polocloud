package dev.httpmarco.polocloud.suite.platforms.commands;

import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.platforms.PlatformProvider;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class PlatformCommand extends Command {

    public PlatformCommand(PlatformProvider platformProvider) {
        super("platform", "Manage all your used platforms");


        syntax(it -> {
            if (platformProvider.platforms().isEmpty()) {
                log.info("No platforms found.");
                return;
            }

            log.info("&7Available platforms &8(&7{}&8):", platformProvider.platforms().size());
            for (var platform : platformProvider.platforms()) {
                log.info("&8  - &7{} &8(&7type&8=&7{}&8)", platform.name(), platform.type().name());
            }
        }, new KeywordArgument("list"));
    }
}
