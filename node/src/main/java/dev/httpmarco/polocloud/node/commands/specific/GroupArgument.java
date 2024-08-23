package dev.httpmarco.polocloud.node.commands.specific;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.CommandArgument;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.node.commands.CommandContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

public final class GroupArgument extends CommandArgument<ClusterGroup> {


    public GroupArgument(String key) {
        super(key);
    }

    @Override
    public boolean predication(@NotNull String rawInput) {
        return Node.instance().groupProvider().exists(rawInput);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String wrongReason() {
        return "The Argument " + key() + " is not a registered cluster group!";
    }

    @Override
    public @NotNull @Unmodifiable List<String> defaultArgs(CommandContext context) {
        return Node.instance().groupProvider().groups().stream().map(Named::name).toList();
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClusterGroup buildResult(String input) {
        return Objects.requireNonNull(Node.instance().groupProvider().find(input));
    }
}
