package dev.httpmarco.polocloud.node.terminal.utils;

import dev.httpmarco.polocloud.node.terminal.impl.JLineTerminalImpl;
import dev.httpmarco.polocloud.node.utils.ManifestReader;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TerminalHeader {

    public void print(JLineTerminalImpl terminal) {
        terminal.printLine("");
        terminal.printLine("   &fPoloCloud &8- &7Simple minecraft cloudsystem &8- &7v" + ManifestReader.detectVersion());
        terminal.printLine("   &7Local node&8: &7null &8| &7External nodes&8: &8[&7"+"&8]");
        terminal.printLine("");
    }

}
