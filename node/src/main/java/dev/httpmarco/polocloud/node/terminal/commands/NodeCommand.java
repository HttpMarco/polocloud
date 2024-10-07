package dev.httpmarco.polocloud.node.terminal.commands;

import dev.httpmarco.polocloud.launcher.update.AutoUpdateInstaller;
import dev.httpmarco.polocloud.launcher.util.OperatingSystem;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.commands.Command;
import dev.httpmarco.polocloud.node.commands.CommandArgumentType;
import lombok.extern.log4j.Log4j2;

import java.io.File;

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

            if (Node.instance().autoUpdater().isInstalled()) {
                log.error(" ");
                log.error("An update is already downloaded.");
                log.error("Please confirm the installation by executing the \"node update confirm\" command.");
                log.error(" ");
                return;
            }

            Node.instance().autoUpdater().update();
        }, "Update your current Node&8.", CommandArgumentType.Keyword("update"));

        syntax(context -> {
            var downloadName = Node.instance().autoUpdater().downloadName();

            if (!Node.instance().autoUpdater().isInstalled() || downloadName == null) {
                log.error("No update has been downloaded yet.");
                return;
            }

            AutoUpdateInstaller.installUpdate(new File(downloadName));
        }, "Confirm the Update on the current Node&8.", CommandArgumentType.Keyword("update"), CommandArgumentType.Keyword("confirm"));
    }
}