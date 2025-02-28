package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.suite.cluster.TestServiceImpl;
import dev.httpmarco.polocloud.suite.cluster.common.AbstractSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public final class LocalSuite extends AbstractSuite {

    private final Server server;

    public LocalSuite(SuiteData data) {
        super(data);
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
    public void updateInfoSnapshot() {

    }
}