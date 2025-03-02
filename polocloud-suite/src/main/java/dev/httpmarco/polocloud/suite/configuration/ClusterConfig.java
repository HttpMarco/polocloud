package dev.httpmarco.polocloud.suite.configuration;

import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;

import java.util.UUID;

public final class ClusterConfig {

    private final String clusterToken;
    private final SuiteData localSuite;
    private final SuiteData[] externalSuites;

    public ClusterConfig() {
        this.clusterToken = UUID.randomUUID().toString().substring(0, 8);
        this.localSuite = new SuiteData("suite-1", "127.0.0.1", 8439);
        this.externalSuites = new SuiteData[0];
    }

    public SuiteData localSuite() {
        return this.localSuite;
    }

    public String clusterToken() {
        return this.clusterToken;
    }

    public SuiteData[] externalSuites() {
        return this.externalSuites;
    }
}
