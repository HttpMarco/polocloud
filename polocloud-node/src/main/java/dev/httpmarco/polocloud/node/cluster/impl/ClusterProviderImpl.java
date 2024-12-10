package dev.httpmarco.polocloud.node.cluster.impl;

import dev.httpmarco.netline.Net;
import dev.httpmarco.netline.cluster.NetCluster;
import dev.httpmarco.netline.cluster.NetNodeData;
import dev.httpmarco.netline.cluster.impl.NetClusterImpl;
import dev.httpmarco.polocloud.node.NodeShutdown;
import dev.httpmarco.polocloud.node.cluster.ClusterData;
import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ClusterProviderImpl extends NetClusterImpl<ClusterData> implements ClusterProvider {

    private final NetCluster<NetNodeData> cluster = Net.line().cluster();

    public ClusterProviderImpl() {
        this.cluster.boot().sync();

        if(!this.cluster.available()) {
            log.warn("The cluster cannot be started. Shutting down...");
            NodeShutdown.nodeShutdownTotal(true);
            return;
        }
        log.info("The cluster has been started successfully!");
    }
}
