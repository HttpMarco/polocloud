package dev.httpmarco.polocloud.sdk.java;

import dev.httpmarco.polocloud.sdk.java.events.EventProvider;
import dev.httpmarco.polocloud.sdk.java.groups.GroupProvider;
import dev.httpmarco.polocloud.sdk.java.logger.LoggerProvider;
import dev.httpmarco.polocloud.sdk.java.platform.PlatformProvider;
import dev.httpmarco.polocloud.sdk.java.player.PlayerProvider;
import dev.httpmarco.polocloud.sdk.java.services.ServiceProvider;
import dev.httpmarco.polocloud.sdk.java.information.CloudInformationProvider;
import dev.httpmarco.polocloud.shared.PolocloudShared;
import dev.httpmarco.polocloud.shared.events.SharedEventProvider;
import dev.httpmarco.polocloud.shared.groups.Group;
import dev.httpmarco.polocloud.shared.groups.SharedGroupProvider;
import dev.httpmarco.polocloud.shared.logging.Logger;
import dev.httpmarco.polocloud.shared.platform.Platform;
import dev.httpmarco.polocloud.shared.platform.SharedPlatformProvider;
import dev.httpmarco.polocloud.shared.player.PolocloudPlayer;
import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider;
import dev.httpmarco.polocloud.shared.service.Service;
import dev.httpmarco.polocloud.shared.service.SharedServiceProvider;
import dev.httpmarco.polocloud.shared.information.SharedCloudInformationProvider;
import dev.httpmarco.polocloud.shared.information.CloudInformation;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jetbrains.annotations.NotNull;

public final class Polocloud extends PolocloudShared {

    private static Polocloud instance;

    private final SharedEventProvider eventProvider;
    private final SharedServiceProvider<Service> serviceProvider;
    private final SharedGroupProvider<Group> groupProvider;
    private final SharedPlayerProvider<PolocloudPlayer> playerProvider;
    private final SharedCloudInformationProvider<CloudInformation> cloudInformationProvider;
    private final Logger logger;
    private final SharedPlatformProvider<Platform> platformProvider;

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
        this.playerProvider = new PlayerProvider(channel);
        this.cloudInformationProvider = new CloudInformationProvider(channel);
        this.logger = new LoggerProvider(channel);
        this.platformProvider = new PlatformProvider(channel);
    }

    public String selfServiceName() {
        return System.getenv("service-name");
    }

    @Override
    public @NotNull SharedEventProvider eventProvider() {
        return this.eventProvider;
    }

    @Override
    public @NotNull SharedServiceProvider<Service> serviceProvider() {
        return this.serviceProvider;
    }

    @Override
    @NotNull
    public SharedGroupProvider<Group> groupProvider() {
        return this.groupProvider;
    }

    @Override
    public @NotNull SharedPlayerProvider<?> playerProvider() {
        return this.playerProvider;
    }

    @Override
    public @NotNull SharedCloudInformationProvider<?> cloudInformationProvider() {
        return this.cloudInformationProvider;
    }

    @Override
    public @NotNull Logger logger() {
        return this.logger;
    }

    @Override
    @NotNull
    public SharedPlatformProvider<?> platformProvider() {
        return this.platformProvider;
    }
}
