package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.terminal.setup.impl.PlatformSetup;
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

        syntax(it -> new PlatformSetup().run(), CommandArgumentType.Keyword("setup"));

        var platformArg = CommandArgumentType.Platform("platform");
        syntax(it -> {
            var platform = it.arg(platformArg);
            var platformService = Node.instance().platformService();

            if (platform == null) {
                log.info("The giving platform is not existing!");
                return;
            }

            if (Node.instance().serviceProvider().services().stream().map(service -> service.group().platform().platform()).anyMatch(id -> id.equalsIgnoreCase(platform.id()))) {
                log.info("The platform is currently in use by a service!");
                return;
            }

            platformService.platforms().remove(platform);
            platformService.update();
        }, platformArg, CommandArgumentType.Keyword("remove"));

        var versionArg = CommandArgumentType.PlatformVersion("version");
        syntax(it -> {
            var platform = it.arg(platformArg);
            var platformVersion = it.arg(versionArg);
            var platformService = Node.instance().platformService();

            if (platform == null) {
                log.info("The giving platform is not existing!");
                return;
            }

            if (platformVersion == null) {
                log.info("The giving version is not existing!");
                return;
            }

            if (Node.instance().serviceProvider().services().stream().map(service -> service.group().platform()).anyMatch(id -> id.platform().equalsIgnoreCase(platform.id()) && id.version().equalsIgnoreCase(platformVersion.version()))) {
                log.info("The platform version is currently in use by a service!");
                return;
            }

            platform.versions().removeIf(version -> version.version().equalsIgnoreCase(platformVersion.version()));
            platformService.update();
        }, platformArg, CommandArgumentType.Keyword("version"), versionArg, CommandArgumentType.Keyword("remove"));

        syntax(it -> {
            var platform = it.arg(platformArg);

            if (platform == null) {
                log.info("The giving platform is not existing!");
                return;
            }

            log.info("Name&8: &b{}", platform.id());
            log.info("Plugin Dir&8: &b{}", platform.pluginDir());
            log.info("Plugin Data&8: &b{}", platform.pluginData());
            log.info("Plugin Data Path&8: &b{}", platform.pluginDataPath());
            log.info("Start Arguments&8: &b{}", platform.arguments());
            log.info("Versions &8(&b{}&8): &7{}", platform.versions().size(), String.join(", ", platform.versions().stream().map(PlatformVersion::version).toList()));
        }, platformArg, CommandArgumentType.Keyword("info"));
    }
}
