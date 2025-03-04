package dev.httpmarco.polocloud.suite.configuration;

import dev.httpmarco.polocloud.suite.cluster.data.LocalSuiteData;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class ClusterConfig {

    @Setter
    private String clusterToken;
    private final LocalSuiteData localSuite;
    private final List<SuiteData> externalSuites;

    public ClusterConfig() {
        this.localSuite = new LocalSuiteData("suite-1", "127.0.0.1", 8439);
        this.externalSuites = new ArrayList<>();
    }
}
