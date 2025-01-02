package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.cluster.config.LocalNodeConfig;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class NodeConfiguration {

    private final String language;
    private final LocalNodeConfig localNode;


    public NodeConfiguration() {
        this.language = "en";
        this.localNode = new LocalNodeConfig("node-1", "127.0.0.1", 9091);
    }
}
