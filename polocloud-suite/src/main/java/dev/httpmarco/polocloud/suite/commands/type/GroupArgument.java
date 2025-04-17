package dev.httpmarco.polocloud.suite.commands.type;

import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.commands.CommandArgument;
import dev.httpmarco.polocloud.suite.commands.CommandContext;

import java.util.List;

public final class GroupArgument extends CommandArgument<ClusterGroup> {

    public GroupArgument(String key) {
        super(key);
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return PolocloudSuite.instance().groupProvider().findAll().stream().map(Named::name).toList();
    }

    @Override
    public ClusterGroup buildResult(String input) {
        return PolocloudSuite.instance().groupProvider().find(input);
    }
}
