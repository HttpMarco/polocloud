package dev.httpmarco.polocloud.suite.cluster;

public interface ClusterProvider {

    ClusterSuite local();

    ClusterSuite head();

}
