package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.node.Node;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public final class ClusterServiceQueue extends Thread {

    public ClusterServiceQueue() {
        super("cluster-node-service-queue");
    }

    @SneakyThrows
    @Override
    public void run() {

        var groupService = Node.instance().groupProvider();
        var localNode = Node.instance().clusterProvider().localNode();

        while (!localNode.situation().isStopping()) {

            if (Node.instance().clusterProvider().headNode() == null) {
                // some cloud ticks is the head node null for new calculation
                continue;
            }

            if (!Node.instance().clusterProvider().localHead()) {
                continue;
            }

            for (ClusterGroup group : groupService.groups()) {

                if (Arrays.stream(group.nodes()).noneMatch(it -> it.equalsIgnoreCase(localNode.data().name()))) {
                    continue;
                }

                if (group.serviceCount() >= group.maxOnlineServerInstances()) {
                    continue;
                }

                var differenceGroupServices = group.minOnlineServerInstances() - group.serviceCount();

                for (long i = 0; i < differenceGroupServices; i++) {
                    Node.instance().serviceProvider().factory().runGroupService(group);
                }

                if (group.properties().has(GroupProperties.PERCENTAGE_TO_START_NEW_SERVER)) {
                    // check percentage to start a new service
                    var globalMaxPlayers = group.services().stream().mapToInt(ClusterService::maxPlayers).sum();
                    var globalPlayers = group.services().stream().mapToInt(ClusterService::onlinePlayersCount).sum();
                    var globalPlayerPercentage = (double) globalPlayers / globalMaxPlayers * 100;
                    var maxPercentage = group.properties().property(GroupProperties.PERCENTAGE_TO_START_NEW_SERVER);

                    if(globalPlayerPercentage >= maxPercentage) {
                        Node.instance().serviceProvider().factory().runGroupService(group);
                    }
                }
            }
            Thread.sleep(1000);
        }
    }
}
