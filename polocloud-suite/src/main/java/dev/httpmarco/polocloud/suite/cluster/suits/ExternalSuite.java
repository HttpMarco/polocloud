package dev.httpmarco.polocloud.suite.cluster.suits;

import dev.httpmarco.polocloud.suite.cluster.common.AbstractSuite;
import dev.httpmarco.polocloud.suite.cluster.data.SuiteData;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public final class ExternalSuite extends AbstractSuite {

    private final ManagedChannel channel;

    public ExternalSuite(SuiteData data) {
        super(data);

        this.channel = ManagedChannelBuilder.forAddress(data().hostname(), data().port()).usePlaintext().build();
    }

    @Override
    public void updateInfoSnapshot() {

    }

    public ConnectivityState available() {
        return this.channel.getState(false);
    }
}
