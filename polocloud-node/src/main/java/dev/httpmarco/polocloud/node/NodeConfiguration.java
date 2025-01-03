package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.cluster.config.LocalNodeConfig;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Locale;

@Getter
@Accessors(fluent = true)
public final class NodeConfiguration {

    private final Locale language;
    private final LocalNodeConfig localNode;


    public NodeConfiguration() {
        this.language = Locale.ENGLISH;
        this.localNode = new LocalNodeConfig("node-1", "127.0.0.1", 9091);
    }
}
