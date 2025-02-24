package dev.httpmarco.polocloud.component.terminal.command;

import dev.httpmarco.polocloud.component.terminal.command.arguments.KeywordArgument;

import java.util.Arrays;

public final class CommandSyntax {

    private final CommandExecution execution;
    private final CommandArgument<?>[] arguments;
    private final String description;

    public CommandSyntax(CommandExecution execution, CommandArgument<?>[] arguments, String description) {
        this.execution = execution;
        this.arguments = arguments;
        this.description = description;
    }

    public CommandSyntax(CommandExecution execution, CommandArgument<?>[] arguments) {
        this(execution, arguments, null);
    }

    public CommandExecution execution() {
        return execution;
    }

    public CommandArgument<?>[] arguments() {
        return arguments;
    }

    public String description() {
        return description;
    }

    public String usage() {
        return String.join(" ", Arrays.stream(arguments)
                .map(it -> it instanceof KeywordArgument ? "&f" + it.key() : "&8<&f" + it.key() + "&8>")
                .toList()) +
                (description == null ? "" : " &8- &7" + description);
    }
}