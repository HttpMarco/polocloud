package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;

public interface ClusterSuite {

    SuiteData data();

    ClusterSuiteState state();

}
