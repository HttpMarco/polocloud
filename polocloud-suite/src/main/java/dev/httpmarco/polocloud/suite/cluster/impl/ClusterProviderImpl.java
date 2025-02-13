package dev.httpmarco.polocloud.suite.cluster.impl;

import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.data.LocalSuiteData;
import dev.httpmarco.polocloud.suite.cluster.suits.LocalSuite;

public final class ClusterProviderImpl implements ClusterProvider {

    private final LocalSuite localSuite;

    public ClusterProviderImpl(LocalSuiteData localSuiteData) {
        this.localSuite = new LocalSuite(localSuiteData);
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
