package dev.httpmarco.polocloud.node.commands.specific;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.CommandArgument;
import dev.httpmarco.polocloud.node.commands.CommandContext;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class PlatformArgument extends CommandArgument<Platform> {

    public PlatformArgument(String key) {
        super(key);
    }

    @Override
    public boolean predication(@NotNull String rawInput) {
        return Node.instance().platformService().exists(rawInput);
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return Arrays.stream(Node.instance().platformService().platforms()).map(Platform::platform).toList();
    }

    @Contract(pure = true)
    @Override
    public @Nullable Platform buildResult(String input) {
        return Node.instance().platformService().platform(input);
    }
}
