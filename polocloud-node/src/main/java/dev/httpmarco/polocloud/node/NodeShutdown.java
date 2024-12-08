package dev.httpmarco.polocloud.node;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NodeShutdown {

    /**
     * Shuts down the node with strg c mode
     * @param directShutdown if the shutdown is direct
     */
    public void nodeShutdownTotal(boolean directShutdown) {
        shutdownNode(directShutdown, true);
    }

    public void shutdownNode(boolean directShutdown, boolean completely) {
        Node.instance().terminal().close();

        if (completely) {
            System.exit(0);
        }
    }
}
