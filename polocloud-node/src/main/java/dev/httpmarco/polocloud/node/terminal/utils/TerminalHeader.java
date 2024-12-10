package dev.httpmarco.polocloud.node.terminal.utils;

import dev.httpmarco.polocloud.launcher.PoloCloud;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.impl.JLineNodeTerminalImpl;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class TerminalHeader {

    // we not need to print this in the logs
    public void print(@NotNull JLineNodeTerminalImpl terminal) {
        terminal.printLine("");
        terminal.printLine("   " + Node.translation().get("terminal.header.description", PoloCloud.version()));
        terminal.printLine("   " + Node.translation().getDefault("terminal.header.information", null, "")); //todo
        terminal.printLine("");
    }

}
