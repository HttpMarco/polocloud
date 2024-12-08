package dev.httpmarco.polocloud.node.terminal.utils;

import dev.httpmarco.polocloud.node.terminal.impl.JLineTerminalImpl;
import dev.httpmarco.polocloud.node.utils.ManifestReader;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class TerminalHeader {

    // we not need to print this in the logs
    public void print(@NotNull JLineTerminalImpl terminal) {
        terminal.printLine("");
        terminal.printLine("   &fPoloCloud &8- &7Simple minecraft cloudsystem &8- &7v" + ManifestReader.detectVersion());
        terminal.printLine("   &7Local node&8: &7null &8| &7External nodes&8: &8[&7"+"&8]");
        terminal.printLine("");
    }

}
