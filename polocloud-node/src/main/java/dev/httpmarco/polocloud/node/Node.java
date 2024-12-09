package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.cluster.impl.ClusterProviderImpl;
import dev.httpmarco.polocloud.node.terminal.Terminal;
import dev.httpmarco.polocloud.node.terminal.impl.JLineTerminalImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Accessors(fluent = true)
public final class Node {

    @Getter
    private static Node instance;

    private final ClusterProvider clusterProvider;
    private final Terminal terminal;

    public Node() {
        instance = this;

        this.terminal = new JLineTerminalImpl();
        this.clusterProvider = new ClusterProviderImpl();
    }
}
