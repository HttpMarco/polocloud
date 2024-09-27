package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.node.cluster.NodeEndpointData;
import dev.httpmarco.polocloud.node.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(fluent = true)
public final class NodeConfig {

    private String clusterId;
    private String clusterToken;
    private final NodeEndpointData localNode;
    private final Set<NodeEndpointData> nodes;

    private final PropertiesPool propertiesPool;
    private final boolean checkForUpdates;

    public NodeConfig() {
        this.clusterId = "polocloud-cluster";
        this.clusterToken = StringUtils.randomString(8);
        this.localNode = new NodeEndpointData("node-" + StringUtils.randomString(4), "127.0.0.1", 9090);
        this.nodes = new HashSet<>();

        this.propertiesPool = new PropertiesPool();
        this.checkForUpdates = true;
    }
}
