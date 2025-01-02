package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.api.PolocloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.common.configuration.Configuration;
import dev.httpmarco.polocloud.node.cluster.ClusterNodeProvider;
import dev.httpmarco.polocloud.node.components.ComponentProvider;
import dev.httpmarco.polocloud.node.components.impl.ComponentProviderImpl;
import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.node.group.ClusterGroupProviderImpl;
import dev.httpmarco.polocloud.node.i18n.I18n;
import dev.httpmarco.polocloud.node.i18n.impl.I18nPolocloudNode;
import dev.httpmarco.polocloud.node.platforms.PlatformProvider;
import dev.httpmarco.polocloud.node.platforms.impl.PlatformProviderImpl;
import dev.httpmarco.polocloud.node.sync.SyncProvider;
import dev.httpmarco.polocloud.node.sync.local.LocalSyncProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Accessors(fluent = true)
public final class Node extends PolocloudAPI {

    @Getter
    private static Node instance;
    @Getter
    private static final I18n translation = new I18nPolocloudNode();

    private final SyncProvider syncProvider;

    private final DependencyProvider dependencyProvider;
    private final ComponentProvider componentProvider;
    private final PlatformProvider platformProvider;
    private final ClusterGroupProvider groupProvider;
    private final ClusterNodeProvider clusterProvider;

    private final Configuration<NodeConfiguration> nodeConfiguration;

    public Node() {
        instance = this;
        log.info(translation.get("node.starting"));

        this.syncProvider = new LocalSyncProvider();
        this.dependencyProvider = new DependencyProviderImpl();
        this.dependencyProvider.loadDefaults();

        log.info("Load local configuration...");
        this.nodeConfiguration = new Configuration<>("config.json", new NodeConfiguration());

        this.platformProvider = new PlatformProviderImpl();
        this.clusterProvider = new ClusterNodeProvider();
        this.componentProvider = new ComponentProviderImpl();
        this.groupProvider = new ClusterGroupProviderImpl();

        log.info("Polocloud Node started successfully!");

    }
}
