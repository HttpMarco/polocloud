package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.suite.cluster.ClusterSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import io.grpc.ManagedChannelBuilder;

public class ExternalSuite implements ClusterSuite {

    private SuiteData data;

    public ExternalSuite(SuiteData data) {
        this.data = data;
    }

    public void tryBind() {
        var channel = ManagedChannelBuilder.forAddress(data.hostname(), data.port()).build();


    }

    @Override
    public SuiteData data() {
        return data;
    }

}
