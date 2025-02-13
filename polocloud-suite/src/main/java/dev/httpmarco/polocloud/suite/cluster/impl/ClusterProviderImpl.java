package dev.httpmarco.polocloud.suite.cluster.impl;

import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.suits.LocalSuite;

public final class ClusterProviderImpl implements ClusterProvider {

    private final LocalSuite localSuite;

    public ClusterProviderImpl() {
        this.localSuite = new LocalSuite();
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
