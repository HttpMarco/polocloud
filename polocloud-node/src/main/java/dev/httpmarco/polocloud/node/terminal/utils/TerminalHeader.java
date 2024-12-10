package dev.httpmarco.polocloud.node.terminal.utils;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.terminal.impl.JLineTerminalImpl;
import dev.httpmarco.polocloud.node.utils.ManifestReader;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class TerminalHeader {

    // we not need to print this in the logs
    public void print(@NotNull JLineTerminalImpl terminal) {
        terminal.printLine("");
        terminal.printLine("   " + Node.translation().get("terminal.header.description", ManifestReader.detectVersion()));
        terminal.printLine("   " + Node.translation().getDefault("terminal.header.information", null, "")); //todo
        terminal.printLine("");
    }

}
