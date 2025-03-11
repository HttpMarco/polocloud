package dev.httpmarco.polocloud.suite.commands.impl;

import dev.httpmarco.polocloud.suite.PolocloudShutdownHandler;

public final class ShutdownCommand extends dev.httpmarco.polocloud.suite.commands.Command {

    public ShutdownCommand() {
        super("shutdown", "Shutdown this local suite", "stop");

        defaultExecution(commandContext -> PolocloudShutdownHandler.shutdown());
    }
}
