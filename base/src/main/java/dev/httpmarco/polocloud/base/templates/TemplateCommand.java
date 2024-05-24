package dev.httpmarco.polocloud.base.templates;

import dev.httpmarco.polocloud.base.terminal.commands.Command;
import dev.httpmarco.polocloud.base.terminal.commands.DefaultCommand;

@Command(command = "templates", description = "Manage or merge templates")
public final class TemplateCommand {

    @DefaultCommand
    public void handle() {

    }
}
