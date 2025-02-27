package dev.httpmarco.polocloud.suite.cluster.impl;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.suits.LocalSuite;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;
import dev.httpmarco.polocloud.suite.utils.ConsoleActions;

public final class ClusterProviderImpl implements ClusterProvider {

    private final LocalSuite localSuite;

    public ClusterProviderImpl(ClusterConfig clusterConfig) {
        this.localSuite = new LocalSuite(clusterConfig.localSuite());

        // spacer between this important information
        ConsoleActions.emptyLine();

        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.header", clusterConfig.localSuite().id()));
        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.element", " \uD83D\uDC51 &b","suite-3", "&8(&737.115.92.27&8@&79877&8, &7state&8=&7AVAILABLE&8)"));
        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.element", "&8   ", "suite-2", "&8(&737.115.92.01&8@&79877&8, &7state&8=&7NOT_AVAILABLE&8)"));
        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.end", "&8   ","suite-4", "&8(&737.115.92.14&8@&79877&8, &7state&8=&7NOT_AVAILABLE&8)"));

        ConsoleActions.emptyLine();
    }

    @Override
    public ClusterSuite local() {
        return this.localSuite;
    }

    @Override
    public ClusterSuite head() {
        return null;
    }
}
