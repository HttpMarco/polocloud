package dev.httpmarco.polocloud.suite.cluster;

public interface ClusterConfig {

    String id();

    int port();
    // hostname not used, because we only run on localhost

}
