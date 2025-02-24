package dev.httpmarco.polocloud.component.terminal.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Command {

    private final String name;
    private final String[] aliases;
    private final String description;

    private CommandExecution defaultExecution;

    private final List<CommandSyntax> commandSyntaxes;

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;

        this.commandSyntaxes = new ArrayList<>();
    }

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
    }

    public String description() {
        return description;
    }

    public CommandExecution defaultExecution() {
        return defaultExecution;
    }

    public void defaultExecution(CommandExecution execution) {
        this.defaultExecution = execution;
    }

    public List<CommandSyntax> commandSyntaxes() {
        return Collections.unmodifiableList(commandSyntaxes);
    }

    public boolean hasSyntaxCommands() {
        return !this.commandSyntaxes.isEmpty();
    }

    public void syntax(CommandExecution execution, CommandArgument<?>... arguments) {
        this.commandSyntaxes.add(new CommandSyntax(execution, arguments, null));
    }

    public void syntax(CommandExecution execution, String description, CommandArgument<?>... arguments) {
        this.commandSyntaxes.add(new CommandSyntax(execution, arguments, description));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Command command && command.name.equals(name);
    }
}