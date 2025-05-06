package dev.httpmarco.polocloud.suite.platforms.commands;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.commands.type.PlatformArgument;
import dev.httpmarco.polocloud.suite.platforms.PlatformProvider;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class PlatformCommand extends Command {

    public PlatformCommand(PlatformProvider platformProvider) {
        super("platforms", "Manage all your used platforms");

        var translation = PolocloudSuite.instance().translation();

        syntax(it -> {
            if (platformProvider.platforms().isEmpty()) {
                log.info(translation.get("suite.command.platform.noneFound"));
                return;
            }

            log.info(translation.get("suite.command.platform.list", platformProvider.platforms().size()));
            for (var platform : platformProvider.platforms()) {
                log.info(translation.get("suite.command.platform.entry", platform.name(), platform.type().name()));
            }
        }, new KeywordArgument("list"));

        var platformArg = new PlatformArgument("platforms");
        syntax(commandContext -> {

            var platform = commandContext.arg(platformArg);

            if(platform == null) {
                log.info(translation.get("suite.command.platform.notFound"));
                return;
            }

            log.info("&7{}&8:", platform.name());
            log.info(translation.get("suite.command.platform.detail.type", platform.type().name()));
            log.info(translation.get("suite.command.platform.detail.language", platform.language()));
            log.info(translation.get("suite.command.platform.detail.filePrep", platform.filePrepareProcess().size()));

            for (int i = 0; i < platform.filePrepareProcess().size(); i++) {
                var fileProcess = platform.filePrepareProcess().get(i);
                var prefix = "├─";

                if(i == platform.filePrepareProcess().size()-1) {
                    prefix = "└─";
                }
                //todo fix duplication
                log.info("&8    {} &7{}", prefix, fileProcess.name());
            }



            log.info(translation.get("suite.command.platform.detail.versions", platform.versions().size()));

            for (int i = 0; i < platform.versions().size(); i++) {
                var version = platform.versions().get(i);
                var prefix = "├─";

                if(i == platform.versions().size()-1) {
                    prefix = "└─";
                }
                log.info("&8    {} &7{}", prefix, version.version());
            }
        }, new KeywordArgument("info"), platformArg);
    }
}
