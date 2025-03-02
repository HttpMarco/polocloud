package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.api.Closeable;
import dev.httpmarco.polocloud.suite.cluster.suits.ExternalSuite;

import java.util.List;

public interface ClusterProvider extends Closeable {

    ClusterSuite local();

    ClusterSuite head();

    List<ExternalSuite> suites();

    void selectHeadSuite();

    void updateData();

}
