package dev.httpmarco.polocloud.suite.platforms.commands;

import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.commands.type.PlatformArgument;
import dev.httpmarco.polocloud.suite.platforms.PlatformProvider;
import dev.httpmarco.polocloud.suite.platforms.setup.PlatformSetup;
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

        syntax(it -> new PlatformSetup().run(), "Create your own custom platform", new KeywordArgument("custom"));

        var platformArg = new PlatformArgument("platform");
        syntax(commandContext -> {

            var platform = commandContext.arg(platformArg);

            if(platform == null) {
                log.info("No platform found.");
                return;
            }

            log.info("Platform &7{}&8:", platform.name());
            log.info("&8  - &7Type&8: &f{}", platform.type().name());
            log.info("&8  - &7Language&8: &f{}", platform.language());
        }, new KeywordArgument("info"), platformArg);
    }
}
