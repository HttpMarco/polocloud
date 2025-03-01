package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import dev.httpmarco.polocloud.suite.cluster.info.ClusterSuiteInfoSnapshot;

public interface ClusterSuite extends Closeable {

    SuiteData data();

    ClusterSuiteState state();

    ClusterSuiteInfoSnapshot infoSnapshot();

    void updateInfoSnapshot();

}