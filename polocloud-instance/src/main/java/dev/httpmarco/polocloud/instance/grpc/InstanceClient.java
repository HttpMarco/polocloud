package dev.httpmarco.polocloud.instance.grpc;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public final class InstanceClient {

    private ManagedChannel channel;

    public InstanceClient() {
        this.channel = ManagedChannelBuilder.forAddress(
                        System.getenv(PolocloudEnvironment.POLOCLOUD_SUITE_HOSTNAME.name()),
                        Integer.parseInt(System.getenv(PolocloudEnvironment.POLOCLOUD_SUITE_PORT.name()))
                ).usePlaintext() // Disable TLS for local development
                .build();
    }

}
