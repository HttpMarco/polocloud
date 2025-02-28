package dev.httpmarco.polocloud.suite.commands.type;

import dev.httpmarco.polocloud.suite.commands.CommandArgument;
import dev.httpmarco.polocloud.suite.commands.CommandContext;

import java.util.List;

public final class KeywordArgument extends CommandArgument<String> {

    public KeywordArgument(String key) {
        super(key);
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return List.of(key());
    }

    @Override
    public String wrongReason() {
        return "";
    }

    @Override
    public String buildResult(String input) {
        return "";
    }

    @Override
    public boolean predication(String rawInput) {
        return super.predication(rawInput) && rawInput.equalsIgnoreCase(key());
    }
}
