package dev.httpmarco.polocloud.suite.cluster.common;

import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.ClusterSuiteState;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.info.ClusterSuiteInfoSnapshot;

public abstract class AbstractSuite<D extends SuiteData> implements ClusterSuite<D> {

    private final D data;
    private ClusterSuiteInfoSnapshot infoSnapshot;
    private ClusterSuiteState state;

    public AbstractSuite(D data) {
        this.data = data;
        this.state = ClusterSuiteState.INITIALIZING;
    }

    @Override
    public D data() {
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
