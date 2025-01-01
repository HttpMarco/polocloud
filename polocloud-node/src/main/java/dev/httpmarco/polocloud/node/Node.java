package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.api.PolocloudAPI;
import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.node.components.ComponentProvider;
import dev.httpmarco.polocloud.node.components.impl.ComponentProviderImpl;
import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.node.dependencies.impl.RepositoryDependency;
import dev.httpmarco.polocloud.node.group.ClusterGroupProviderImpl;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
@Accessors(fluent = true)
public final class Node extends PolocloudAPI {

    private final DependencyProvider dependencyProvider;
    private final ComponentProvider componentProvider;
    private final ClusterGroupProvider groupProvider;


    public Node() {
        log.info("Starting Polocloud Node...");

        this.dependencyProvider = new DependencyProviderImpl();

        this.dependencyProvider.originalSlot().bindDependencies(new RepositoryDependency("org.apache.logging.log4j", "log4j-api", Version.parse("2.24.2")));

        this.componentProvider = new ComponentProviderImpl();
        this.groupProvider = new ClusterGroupProviderImpl();
    }
}
