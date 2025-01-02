package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.netline.cluster.NetNodeData;
import dev.httpmarco.netline.cluster.impl.NetClusterImpl;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ClusterNodeProvider extends NetClusterImpl<NetNodeData> {

    public ClusterNodeProvider() {
        super.boot().sync();

        log.info("Detect head node: {}", this.headNode().toString());
    }
}
