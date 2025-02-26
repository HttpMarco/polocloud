package dev.httpmarco.polocloud.suite.configuration;

import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;

public final class ClusterConfig {

    private final SuiteData localSuite;
    private final SuiteData[] externalSuites;

    public ClusterConfig() {
        this.localSuite = new SuiteData("suite-1", "127.0.0.1", 8439);
        this.externalSuites = new SuiteData[0];
    }

    public SuiteData localSuite() {
        return localSuite;
    }

    public SuiteData[] externalSuites() {
        return externalSuites;
    }
}
