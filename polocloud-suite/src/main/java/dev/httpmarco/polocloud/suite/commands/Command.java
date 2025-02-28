package dev.httpmarco.polocloud.suite.commands;


import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private final String name;
    private final String[] aliases;
    private final String description;

    private CommandExecution defaultExecution;
    private final List<CommandSyntax> commandSyntaxes = new ArrayList<>();

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public String name() {
        return name;
    }

    public String[] aliases() {
        return aliases;
    }

    public void syntax(CommandExecution execution, CommandArgument<?>... arguments) {
        this.commandSyntaxes.add(new CommandSyntax(execution, arguments, null));
    }

    public CommandExecution defaultExecution() {
        return this.defaultExecution;
    }

    public List<CommandSyntax> commandSyntaxes() {
        return this.commandSyntaxes;
    }

    public void syntax(CommandExecution execution, String description, CommandArgument<?>... arguments) {
        this.commandSyntaxes.add(new CommandSyntax(execution, arguments, description));
    }

    public void defaultExecution(CommandExecution execution) {
        this.defaultExecution = execution;
    }

    public boolean hasSyntaxCommands() {
        return !this.commandSyntaxes.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Command command && command.name.equals(name);
    }
}
