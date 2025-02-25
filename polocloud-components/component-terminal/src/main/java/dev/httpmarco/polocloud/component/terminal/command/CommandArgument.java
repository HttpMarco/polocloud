package dev.httpmarco.polocloud.component.terminal.command;

import dev.httpmarco.polocloud.suite.i18n.I18n;

import java.util.List;

public abstract class CommandArgument<T> {

    private final String key;

    public CommandArgument(String key) {
        this.key = key;
    }

    public List<String> defaultArgs(CommandContext context) {
        return List.of();
    }

    // if one argument must be special type
    public boolean predication(String rawInput) {
        return !(rawInput.startsWith("<") && rawInput.endsWith(">"));
    }

    public String wrongReason() {
        return translation().get("terminal.command.argument.wrong");
    }

    public abstract T buildResult(String input);

    public String key() {
        return key;
    }

    public I18n translation() {
        // todo
        return null;
    }

}