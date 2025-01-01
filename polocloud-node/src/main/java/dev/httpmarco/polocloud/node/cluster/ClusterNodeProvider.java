package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.netline.Net;
import dev.httpmarco.netline.cluster.NetCluster;
import dev.httpmarco.netline.cluster.NetNodeData;

public final class ClusterNodeProvider {

    private final NetCluster<NetNodeData> cluster;

    public ClusterNodeProvider() {
        this.cluster = Net.line().cluster();
        this.cluster.boot().sync();
    }
}
