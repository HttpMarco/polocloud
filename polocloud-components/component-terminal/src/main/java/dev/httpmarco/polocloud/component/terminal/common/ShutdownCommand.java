package dev.httpmarco.polocloud.component.terminal.common;

import dev.httpmarco.polocloud.component.api.ComponentSuite;
import dev.httpmarco.polocloud.component.terminal.command.Command;

public final class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", "Shutdown the current suite", "stop", "exit");

        this.defaultExecution(commandContext -> ComponentSuite.instance().suiteSystemProvider().shutdown());
    }
}
