package dev.httpmarco.polocloud.suite.cluster.local;

import dev.httpmarco.polocloud.suite.cluster.Cluster;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.grpc.ClusterGrpcServer;
import org.jetbrains.annotations.NotNull;

public class LocalCluster extends ClusterGrpcServer implements Cluster {

    public LocalCluster(@NotNull ClusterSuiteData data) {
        super(data);
    }

    @Override
    public String name() {
        return "local";
    }
}
