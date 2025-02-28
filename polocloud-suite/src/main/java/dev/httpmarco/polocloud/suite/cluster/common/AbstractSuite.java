package dev.httpmarco.polocloud.suite.cluster.common;

import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuiteState;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.info.ClusterSuiteInfoSnapshot;

public abstract class AbstractSuite implements ClusterSuite {

    private final SuiteData data;
    private ClusterSuiteInfoSnapshot infoSnapshot;
    private ClusterSuiteState state;

    public AbstractSuite(SuiteData data) {
        this.data = data;
        this.state = ClusterSuiteState.INITIALIZING;
    }

    @Override
    public SuiteData data() {
        return data;
    }

    @Override
    public ClusterSuiteState state() {
        return state;
    }

    public void state(ClusterSuiteState state) {
        this.state = state;
    }

    @Override
    public ClusterSuiteInfoSnapshot infoSnapshot() {
        return this.infoSnapshot;
    }
}
