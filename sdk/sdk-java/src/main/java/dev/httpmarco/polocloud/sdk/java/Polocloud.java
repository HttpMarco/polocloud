package dev.httpmarco.polocloud.sdk.java;

import dev.httpmarco.polocloud.sdk.java.events.EventProvider;
import dev.httpmarco.polocloud.sdk.java.groups.GroupProvider;
import dev.httpmarco.polocloud.sdk.java.services.ServiceProvider;
import dev.httpmarco.polocloud.shared.PolocloudShared;
import dev.httpmarco.polocloud.shared.events.SharedEventProvider;
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider;
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jetbrains.annotations.NotNull;

public final class Polocloud extends PolocloudShared {

    private static Polocloud instance;

    private final SharedEventProvider eventProvider;
    private final SharedServiceProvider serviceProvider;
    private final SharedGroupProvider groupProvider;

    public static Polocloud instance() {
        return instance;
    }

    static {
        new Polocloud();
    }

    Polocloud() {
        instance = this;

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", Integer.parseInt(System.getenv("agent_port")))
                .usePlaintext()
                .build();

        this.eventProvider = new EventProvider(channel);
        this.serviceProvider = new ServiceProvider(channel);
        this.groupProvider = new GroupProvider(channel);
    }

    public String selfServiceName() {
        return System.getenv("service-name");
    }

    @Override
    public @NotNull SharedEventProvider eventProvider() {
        return this.eventProvider;
    }

    @Override
    public @NotNull SharedServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    @Override
    @NotNull
    public SharedGroupProvider groupProvider() {
        return this.groupProvider;
    }
}
