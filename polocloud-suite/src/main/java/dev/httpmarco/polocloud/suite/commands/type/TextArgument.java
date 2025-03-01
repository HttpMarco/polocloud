package dev.httpmarco.polocloud.suite.commands.type;


import dev.httpmarco.polocloud.suite.commands.CommandArgument;

public final class TextArgument extends CommandArgument<String> {

    public TextArgument(String key) {
        super(key);
    }

    @Override
    public String buildResult(String input) {
        return input;
    }
}
