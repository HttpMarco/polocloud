package dev.httpmarco.polocloud.base;

public final class NodeShutdownTask {

    public static void run() {

        // node already in the shutdown process
        if (!Node.instance().running()) {
            return;
        }

        var node = Node.instance();
        node.running = false;

        node.logger().info("Shutdown cloud...");
        node.serviceProvider().close();

        for (var service : node.serviceProvider().services()) {
            service.shutdown();
        }

        node.nodeProvider().localEndpoint().server().close();

        node.logger().info("Cloud successfully stopped!");
        node.loggerFactory().close();

        // close application
        System.exit(0);
    }
}
