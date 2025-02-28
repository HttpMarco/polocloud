package dev.httpmarco.polocloud.suite.commands;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import java.util.List;

public abstract class CommandArgument<T> {

    private final String key;

    public CommandArgument(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }

    public List<String> defaultArgs(CommandContext context) {
        return List.of();
    }

    // if one argument must be special type
    public boolean predication(String rawInput) {
        return !(rawInput.startsWith("<") && rawInput.endsWith(">"));
    }

    public String wrongReason() {
        return PolocloudSuite.instance().translation().get("terminal.command.argument.wrong");
    }

    public abstract T buildResult(String input);
}
