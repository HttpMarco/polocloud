package dev.httpmarco.polocloud.component.terminal.command.arguments;

import dev.httpmarco.polocloud.component.terminal.command.CommandArgument;

public final class StringArrayArgument extends CommandArgument<String> {

    public StringArrayArgument(String key) {
        super(key);
    }

    @Override
    public String buildResult(String input) {
        return input;
    }
}