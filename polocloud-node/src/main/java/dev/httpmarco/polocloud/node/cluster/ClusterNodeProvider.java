package dev.httpmarco.polocloud.node.cluster;

import dev.httpmarco.netline.cluster.NetNodeData;
import dev.httpmarco.netline.cluster.impl.NetClusterImpl;
import dev.httpmarco.polocloud.node.Node;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class ClusterNodeProvider extends NetClusterImpl<NetNodeData> {

    public ClusterNodeProvider() {

        localNode().config().id(Node.instance().nodeConfiguration().value().localNode().id());
        super.boot().sync();

        log.info("Detect head node: {}", this.headNode().id());
    }

    public boolean runtimeHead() {
        return this.headNode().equals(this.localNode());
    }
}