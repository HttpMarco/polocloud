package dev.httpmarco.polocloud.suite.commands.type;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.commands.CommandArgument;
import dev.httpmarco.polocloud.suite.commands.CommandContext;

import java.util.List;

public final class ClusterServiceArgument extends CommandArgument<ClusterService> {

    public ClusterServiceArgument(String key) {
        super(key);
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return Polocloud.instance().serviceProvider().findAll().stream().map(Named::name).toList();
    }

    @Override
    public ClusterService buildResult(String input) {
        return Polocloud.instance().serviceProvider().find(input);
    }
}
