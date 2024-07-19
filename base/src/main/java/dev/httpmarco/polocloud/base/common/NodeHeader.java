package dev.httpmarco.polocloud.base.common;

import dev.httpmarco.polocloud.base.Node;
import dev.httpmarco.polocloud.base.node.NodeProvider;
import dev.httpmarco.polocloud.base.terminal.CloudTerminal;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class NodeHeader {

    public static void print(@NotNull CloudTerminal terminal) {
        terminal.spacer();
        terminal.spacer("   &3PoloCloud &2- &1Simple minecraft cloudsystem &2- &1v1.0.10-snapshot");
        terminal.spacer("   &1Local node&2: &1" + localId() + " &2| &1External nodes&2: &1" + separateExternalStringList());
        terminal.spacer();
    }

    private static String localId() {
        return Node.instance().nodeProvider().localEndpoint().data().id();
    }

    @Contract(" -> new")
    private static @NotNull String separateExternalStringList() {
        return String.join(", ", nodeProvider().externalNodeEndpoints().stream().map(externalNodeEndpoint -> externalNodeEndpoint.data().id()).toList());
    }

    private static NodeProvider nodeProvider() {
        return Node.instance().nodeProvider();
    }
}
