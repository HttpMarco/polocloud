package dev.httpmarco.polocloud.component.terminal.command;

import java.util.HashMap;
import java.util.Map;

public final class CommandContext {

    private final Map<String, Object> contexts = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T arg(CommandArgument<T> argument) {
        return (T) contexts.get(argument.key());
    }

    public void append(CommandArgument<?> argument, Object value) {
        this.contexts.put(argument.key(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T arg(String id) {
        return (T) contexts.get(id);
    }
}