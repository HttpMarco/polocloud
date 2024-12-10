package dev.httpmarco.polocloud.node.commands.type;

import dev.httpmarco.polocloud.node.commands.CommandArgument;

public final class StringArrayArgument extends CommandArgument<String> {

    public StringArrayArgument(String key) {
        super(key);
    }

    @Override
    public String buildResult(String input) {
        return input;
    }
}