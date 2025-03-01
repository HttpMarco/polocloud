package dev.httpmarco.polocloud.suite.cluster;

import dev.httpmarco.polocloud.api.Closeable;

public interface ClusterProvider extends Closeable {

    ClusterSuite local();

    ClusterSuite head();

    void selectHeadSuite();

    void updateData();

}
