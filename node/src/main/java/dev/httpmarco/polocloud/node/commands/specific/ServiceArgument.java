package dev.httpmarco.polocloud.node.commands.specific;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.CommandArgument;
import dev.httpmarco.polocloud.node.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ServiceArgument extends CommandArgument<ClusterService> {

    public ServiceArgument(String key) {
        super(key);
    }

    @Override
    public boolean predication(@NotNull String rawInput) {
        return Node.instance().serviceProvider().services().stream().anyMatch(it -> it.name().equalsIgnoreCase(rawInput));
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return Node.instance().serviceProvider().services().stream().map(ClusterService::name).toList();
    }

    @Override
    public ClusterService buildResult(String input) {
        return Node.instance().serviceProvider().find(input);
    }
}
