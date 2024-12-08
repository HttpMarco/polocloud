package dev.httpmarco.polocloud.node.terminal.commands;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public final class CommandContext {

    private final Map<String, Object> contexts = new HashMap<>();

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public <T> T arg(@NotNull CommandArgument<T> argument) {
        return (T) contexts.get(argument.key());
    }

    public void append(@NotNull CommandArgument<?> argument, Object value) {
        this.contexts.put(argument.key(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T arg(String id) {
        return (T) contexts.get(id);
    }
}
