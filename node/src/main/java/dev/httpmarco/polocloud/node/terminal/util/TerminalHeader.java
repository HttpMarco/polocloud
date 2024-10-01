package dev.httpmarco.polocloud.node.terminal.util;

import dev.httpmarco.polocloud.node.NodeConfig;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.terminal.JLineTerminal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TerminalHeader {

    public void print(JLineTerminal terminal, NodeConfig config) {
        terminal.printLine("");
        terminal.printLine("   &fPoloCloud &8- &7Simple minecraft cloudsystem &8- &7v" + System.getProperty("Polocloud-Version"));
        terminal.printLine("   &7Local node&8: &7" + config.localNode().name() + " &8| &7External nodes&8: &8[&7" + String.join("&8, &7", config.nodes().stream().map(NodeEndpointData::name).toList()) +"&8]");
        terminal.printLine("");
    }

}
