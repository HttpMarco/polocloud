package dev.httpmarco.polocloud.instance.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public final class InstanceClient {

    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext() // Disable TLS for local development
            .build();

}
