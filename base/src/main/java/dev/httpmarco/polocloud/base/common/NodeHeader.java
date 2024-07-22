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
        terminal.spacer("   &7PoloCloud &8- &7Simple minecraft cloudsystem &8- &7v1.0.10-snapshot");
        terminal.spacer("   &7Local node&8: &7" + localId() + " &8| &7External nodes&8: &7" + separateExternalStringList());
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
