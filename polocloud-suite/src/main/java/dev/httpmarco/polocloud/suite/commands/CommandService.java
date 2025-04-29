package dev.httpmarco.polocloud.suite.commands;

import dev.httpmarco.polocloud.suite.commands.impl.ClearCommand;
import dev.httpmarco.polocloud.suite.commands.impl.InfoCommand;
import dev.httpmarco.polocloud.suite.commands.impl.ShutdownCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandService {

    private final List<Command> commands = new ArrayList<>();

    public CommandService() {
        this.registerCommand(new ShutdownCommand());
        this.registerCommand(new ClearCommand());
        this.registerCommand(new InfoCommand());
    }

    public List<Command> commandsByName(String name) {
        return commands.stream().filter(it -> it.name().equalsIgnoreCase(name) || Arrays.stream(it.aliases()).anyMatch(s -> s.equalsIgnoreCase(name))).toList();
    }

    public void registerCommand(Command command) {
        this.commands.add(command);

        if (command instanceof RefreshableCommand refreshableCommand) {
            refreshableCommand.loadContext();
        }
    }

    public void registerCommands(Command... commands) {
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

    public List<Command> commands() {
        return commands;
    }

    public void refreshCommandContext() {
        this.commands.stream()
                .filter(it -> it instanceof RefreshableCommand)
                .forEach(command -> {
                    command.commandSyntaxes().clear();
                    ((RefreshableCommand) command).loadContext();
                });
    }
}