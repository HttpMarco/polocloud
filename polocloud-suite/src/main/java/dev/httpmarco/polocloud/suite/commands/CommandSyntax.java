package dev.httpmarco.polocloud.suite.commands;

import dev.httpmarco.polocloud.suite.commands.type.KeywordArgument;

import java.util.Arrays;

public final class CommandSyntax {

    private final CommandExecution execution;
    private final CommandArgument<?>[] arguments;
    private  String description;

    public CommandSyntax(CommandExecution execution, CommandArgument<?>[] arguments, String description) {
        this.execution = execution;
        this.arguments = arguments;
    }

    public  String usage() {
        return String.join(" ", Arrays.stream(arguments)
                .map(it -> it instanceof KeywordArgument ? "&f" + it.key() : "&8<&f" + it.key() + "&8>")
                .toList()) +
                (description == null ? "" : " &8- &7" + description);
    }

    public CommandArgument<?>[] arguments() {
        return arguments;
    }

    public CommandExecution execution() {
        return execution;
    }
}
