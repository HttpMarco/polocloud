package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.suite.cluster.common.AbstractSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import io.grpc.ManagedChannelBuilder;

public class ExternalSuite extends AbstractSuite {

    public ExternalSuite(SuiteData data) {
        super(data);
    }

    public void tryBind() {
        var channel = ManagedChannelBuilder.forAddress(data().hostname(), data().port()).build();


    }
}
