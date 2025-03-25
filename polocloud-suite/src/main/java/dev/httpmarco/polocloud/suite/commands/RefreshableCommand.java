package dev.httpmarco.polocloud.suite.commands;

/**
 * Represents a command that can be refreshed -> context
 */
public abstract class RefreshableCommand extends Command{

    public RefreshableCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    public abstract void loadContext();
}
