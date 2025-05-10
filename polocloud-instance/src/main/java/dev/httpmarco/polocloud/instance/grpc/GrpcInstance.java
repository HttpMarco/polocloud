package dev.httpmarco.polocloud.instance.grpc;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class GrpcInstance {

    // for outgoing information
    private final ManagedChannel channel;
    private final Server server;

    public GrpcInstance() {
        this.channel = ManagedChannelBuilder.forAddress(
                        System.getenv(PolocloudEnvironment.POLOCLOUD_SUITE_HOSTNAME.name()),
                        Integer.parseInt(System.getenv(PolocloudEnvironment.POLOCLOUD_SUITE_PORT.name()))
                ).usePlaintext() // Disable TLS for local development
                .build();

        var port = Integer.parseInt(System.getenv(PolocloudEnvironment.POLOCLOUD_SUITE_PORT.name())) + 1;

        this.server = ServerBuilder.forPort(port)
                .build();
    }
}
