package dev.httpmarco.polocloud.instance.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class InstanceClient {

    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext() // Disable TLS for local development
            .build();

}
