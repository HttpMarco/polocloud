package dev.httpmarco.polocloud.suite.commands.type;

import dev.httpmarco.polocloud.suite.commands.CommandArgument;
import dev.httpmarco.polocloud.suite.commands.CommandContext;

import java.util.List;

public final class IntArgument extends CommandArgument<Integer> {

    public IntArgument(String key) {
        super(key);
    }

    @Override
    public boolean predication(String rawInput) {
        try {
            Integer.parseInt(rawInput);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String wrongReason() {
        return "The argument " + key() + " is not a number!";
    }

    @Override
    public List<String> defaultArgs(CommandContext context) {
        return List.of();
    }

    @Override
    public Integer buildResult(String input) {
        return Integer.parseInt(input);
    }
}
