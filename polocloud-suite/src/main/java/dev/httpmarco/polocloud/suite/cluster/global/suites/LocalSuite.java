package dev.httpmarco.polocloud.suite.cluster.global.suites;

import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.global.ClusterSuiteData;
import dev.httpmarco.polocloud.suite.cluster.grpc.ClusterGrpcServer;
import dev.httpmarco.polocloud.suite.cluster.grpc.ClusterSuiteGrpcHandler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Getter
@Accessors(fluent = true)
public final class LocalSuite extends ClusterGrpcServer implements ClusterSuite {

    public LocalSuite(ClusterSuiteData data) {
       super(data);
    }

    @Override
    public String id() {
        return data().id();
    }

}