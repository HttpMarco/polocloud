package dev.httpmarco.polocloud.suite.cluster.impl;

import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.command.SuiteCommand;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.suits.ExternalSuite;
import dev.httpmarco.polocloud.suite.cluster.suits.LocalSuite;
import dev.httpmarco.polocloud.suite.utils.ConsoleActions;

import java.util.ArrayList;
import java.util.List;

public final class ClusterProviderImpl implements ClusterProvider {

    private final List<ExternalSuite> externalSuites = new ArrayList<>();
    private final LocalSuite localSuite;
    private ClusterSuite<SuiteData> headSuite;

    public ClusterProviderImpl() {
        var clusterConfig = PolocloudSuite.instance().config().cluster();
        this.localSuite = new LocalSuite(clusterConfig.localSuite());

        for (var suiteData : clusterConfig.externalSuites()) {
            this.externalSuites.add(new ExternalSuite(suiteData));
        }

        this.selectHeadSuite();

        if (!this.externalSuites.isEmpty()) {
            System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.header", clusterConfig.localSuite().id()));
        } else {
            System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.element", " \uD83D\uDC51 &b", this.headSuite.data().id(), "&8(&7" + localSuite.data().hostname() + "&8@&7" + this.localSuite.data().port() + "&8, &7state&8=&7" + localSuite.state() + "&8)"));
        }

        for (int i = 0; i < this.externalSuites.size(); i++) {
            var suite = this.externalSuites.get(i);

            if (i == externalSuites.size() - 1) {
                System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.end", "&8    ", "suite-4", "&8(&737.115.92.14&8@&79877&8, &7state&8=&7NOT_AVAILABLE&8)"));
                continue;
            }
            System.out.println(PolocloudSuite.instance().translation().get("suite.cluster.graph.element", "&8    ", "suite-2", "&8(&737.115.92.01&8@&79877&8, &7state&8=&7NOT_AVAILABLE&8)"));

        }

        ConsoleActions.emptyLine();

        // register command for manage all suites
        PolocloudSuite.instance().commandService().registerCommand(new SuiteCommand(this));
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
        if (this.externalSuites.isEmpty()) {
            this.headSuite = localSuite;
            return;
        }

        // load the oldest suite
    }

    @Override
    public List<ExternalSuite> suites() {
        return externalSuites;
    }

    @Override
    public void updateData() {
        // todo update all binded cluster instance
    }

    @Override
    public void close() {
        this.localSuite.close();
    }
}
