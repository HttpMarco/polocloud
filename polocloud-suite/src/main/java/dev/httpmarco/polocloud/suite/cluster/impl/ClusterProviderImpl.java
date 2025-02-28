package dev.httpmarco.polocloud.suite.cluster.impl;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.suits.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.suits.LocalSuite;
import dev.httpmarco.polocloud.suite.configuration.ClusterConfig;
import dev.httpmarco.polocloud.suite.utils.ConsoleActions;

import java.util.ArrayList;
import java.util.List;

public final class ClusterProviderImpl implements ClusterProvider {

    private final List<ExternalSuite> externalSuites = new ArrayList<>();
    private final LocalSuite localSuite;
    private ClusterSuite headSuite;

    public ClusterProviderImpl(ClusterConfig clusterConfig) {
        this.localSuite = new LocalSuite(clusterConfig.localSuite());

        for (var suiteData : clusterConfig.externalSuites()) {
            this.externalSuites.add(new ExternalSuite(suiteData));
        }

        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.header", clusterConfig.localSuite().id()));
        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.element", " \uD83D\uDC51 &b","suite-3", "&8(&737.115.92.27&8@&79877&8, &7state&8=&7AVAILABLE&8)"));
        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.element", "&8    ", "suite-2", "&8(&737.115.92.01&8@&79877&8, &7state&8=&7NOT_AVAILABLE&8)"));
        System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.end", "&8    ","suite-4", "&8(&737.115.92.14&8@&79877&8, &7state&8=&7NOT_AVAILABLE&8)"));

        ConsoleActions.emptyLine();
        //todo verify connection
    }

    @Override
    public ClusterSuite local() {
        return this.localSuite;
    }

    @Override
    public ClusterSuite head() {
        return this.headSuite;
    }

    @Override
    public void selectHeadSuite() {
        if(this.externalSuites.isEmpty()) {
            this.headSuite = localSuite;
            return;
        }

        // load the oldest suite
    }

    @Override
    public void updateData() {
        // todo update all binded cluster instance
    }
}
