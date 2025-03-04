package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.suite.cluster.data.LocalSuiteData;
import dev.httpmarco.polocloud.suite.cluster.grpc.ClusterGrpcServiceImpl;
import dev.httpmarco.polocloud.suite.cluster.common.AbstractSuite;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class LocalSuite extends AbstractSuite<LocalSuiteData> {

    private static final Logger log = LogManager.getLogger(LocalSuite.class);
    private final Server server;

    public LocalSuite(LocalSuiteData data) {
        super(data);
        this.server = ServerBuilder.forPort(data.port()).addService(new ClusterGrpcServiceImpl()).addService(new HealthStatusManager().getHealthService()).build();

        try {
            this.server.start();
            // log.info(PolocloudSuite.instance().translation().get("suite.local.process.start.success", data.port()));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            // todo call shutdown methode
        }
    }

    @Override
    public void updateInfoSnapshot() {

    }

    @Override
    public void close() {
        log.info("Shutting down local suite...");
        this.server.shutdownNow();
    }
}