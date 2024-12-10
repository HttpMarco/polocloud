package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.cluster.ClusterProvider;
import dev.httpmarco.polocloud.node.cluster.impl.ClusterProviderImpl;
import dev.httpmarco.polocloud.node.terminal.NodeTerminal;
import dev.httpmarco.polocloud.node.commands.CommandService;
import dev.httpmarco.polocloud.node.terminal.impl.JLineNodeTerminalImpl;
import dev.httpmarco.polocloud.node.i18n.I18n;
import dev.httpmarco.polocloud.node.i18n.impl.I18nPoloCloudNode;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Accessors(fluent = true)
public final class Node {

    @Getter
    private static Node instance;
    @Getter
    private static final I18n translation = new I18nPoloCloudNode();

    private final ClusterProvider clusterProvider;
    private final NodeTerminal terminal;
    private final CommandService commandService;

    public Node() {
        instance = this;

        this.terminal = new JLineNodeTerminalImpl();
        this.commandService = new CommandService();
        this.clusterProvider = new ClusterProviderImpl();

    }
}
