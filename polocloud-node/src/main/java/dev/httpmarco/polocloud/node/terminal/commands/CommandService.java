package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.node.terminal.commands.*;
import dev.httpmarco.polocloud.node.terminal.commands.impl.ShutdownCommand;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CommandService {

    private final List<Command> commands = new ArrayList<>();

    public CommandService() {
        this.registerCommand(new ShutdownCommand());
    }

    @Contract(pure = true)
    public @Unmodifiable List<Command> commandsByName(String name) {
        return commands.stream().filter(it -> it.name().equalsIgnoreCase(name) || Arrays.stream(it.aliases()).anyMatch(s -> s.equalsIgnoreCase(name))).toList();
    }

    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    public void registerCommands(Command @NotNull ... commands) {
        for (var command : commands) {
            registerCommand(command);
        }
    }

    public void unregisterCommand(Command command) {
        this.commands.remove(command);
    }

    public void call(String commandId, String[] args) {
        CommandParser.serializer(this, commandId, args);
    }
}