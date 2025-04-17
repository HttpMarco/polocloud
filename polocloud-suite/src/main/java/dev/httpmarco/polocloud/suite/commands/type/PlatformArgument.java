package dev.httpmarco.polocloud.suite.commands.type;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.commands.CommandArgument;
import dev.httpmarco.polocloud.suite.commands.CommandContext;
import dev.httpmarco.polocloud.suite.platforms.Platform;

import java.util.List;

public final class PlatformArgument extends CommandArgument<Platform> {

    public PlatformArgument(String key) {
        super(key);
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return PolocloudSuite.instance().platformProvider().platforms().stream().map(Platform::name).toList();
    }

    @Override
    public Platform buildResult(String input) {
        return PolocloudSuite.instance().platformProvider().findPlatform(input);
    }
}
