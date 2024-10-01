package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.launcher.util.OperatingSystem;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class NodeCommand extends Command {

    public NodeCommand() {
        super("node", "Manage your node");

        syntax(context -> {
            var operatingSystem = OperatingSystem.detectOS();

            if (operatingSystem == OperatingSystem.UNKNOWN) {
                log.error("Auto Update is not supportet on your operating system.");
                return;
            }

            Node.instance().autoUpdater().update();
        }, "Update your current Node&8.", CommandArgumentType.Keyword("update"));
    }
}