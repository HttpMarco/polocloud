package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.TestServiceImpl;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public final class LocalSuite implements ClusterSuite {

    private final Server server;

    private final SuiteData data;

    public LocalSuite(SuiteData data) {
        this.data = data;
        this.server = ServerBuilder.forPort(data.port()).addService(new TestServiceImpl()).build();

        try {
            this.server.start();
            // log.info(PolocloudSuite.instance().translation().get("suite.local.process.start.success", data.port()));
        } catch (IOException e) {
            e.printStackTrace(System.err);
            // todo call shutdown methode
        }
    }

    @Override
    public SuiteData data() {
        return data;
    }
}