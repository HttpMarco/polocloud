package dev.httpmarco.polocloud.suite.platforms.commands;

import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;
import dev.httpmarco.polocloud.suite.commands.type.PlatformArgument;
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

        var platformArg = new PlatformArgument("platform");
        syntax(commandContext -> {

            var platform = commandContext.arg(platformArg);

            if(platform == null) {
                log.info("No platform found.");
                return;
            }

            log.info("&7{}&8:", platform.name());
            log.info("&8  ├─ &7Type&8: &f{}", platform.type().name());
            log.info("&8  ├─ &7Language&8: &f{}", platform.language());
            log.info("&8  ├─ &7File preparation&8:(&f{}&8)", platform.filePrepareProcess().size());

            for (int i = 0; i < platform.filePrepareProcess().size(); i++) {
                var fileProcess = platform.filePrepareProcess().get(i);
                var prefix = "├─";

                if(i == platform.filePrepareProcess().size()-1) {
                    prefix = "└─";
                }
                //todo fix duplication
                log.info("&8    {} &7{}", prefix, fileProcess.name());
            }



            log.info("&8  └─ &7Versions&8: (&f{}&8)", platform.versions().size());

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
