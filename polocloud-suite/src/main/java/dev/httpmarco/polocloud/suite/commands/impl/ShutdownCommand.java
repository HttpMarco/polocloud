package dev.httpmarco.polocloud.suite.commands.impl;

import dev.httpmarco.polocloud.suite.PolocloudSuite;

public final class ShutdownCommand extends dev.httpmarco.polocloud.suite.commands.Command {

    public ShutdownCommand() {
        super("shutdown", "Shutdown this local suite", "stop");

        defaultExecution(commandContext -> PolocloudSuite.instance().shutdown());
    }
}
