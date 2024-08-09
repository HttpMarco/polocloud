package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.cluster.NodeSituation;
import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public final class NodeShutdown {

    public static void nodeShutdown(boolean directShutdown) {

        var clusterService = Node.instance().clusterProvider();

        if (clusterService.localNode().situation().isStopping()) {
            return;
        }

        log.info("Start cluster shutdown process &8(&7{}&8)...", directShutdown ? "direct strg+c exit" : "clean exit");
        Node.instance().terminal().commandReadingThread().interrupt();

        log.info("Shutdown all local node services&8...");
        clusterService.localNode().situation(NodeSituation.STOPPING);

        Node.instance().serviceProvider().services().stream()
                .filter(it -> it instanceof ClusterLocalServiceImpl)
                .map(it -> (ClusterLocalServiceImpl) it)
                .forEach(localService -> {
                    if (directShutdown) {
                        // if a command user use strg + c
                        localService.destroyService();
                    } else {
                        localService.shutdown();
                    }
                });


        Node.instance().moduleProvider().unloadAllModules();

        clusterService.close();

        Node.instance().terminal().close();

        clusterService.localNode().situation(NodeSituation.STOPPED);
        System.exit(0);
    }
}
