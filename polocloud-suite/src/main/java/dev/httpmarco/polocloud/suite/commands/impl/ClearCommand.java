package dev.httpmarco.polocloud.suite.commands.impl;

import dev.httpmarco.polocloud.suite.commands.Command;
import dev.httpmarco.polocloud.suite.utils.ConsoleActions;

public final class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Clear the console terminal", "cc");

        defaultExecution(commandContext -> ConsoleActions.clearScreen());
    }
}
