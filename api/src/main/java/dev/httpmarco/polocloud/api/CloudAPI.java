package dev.httpmarco.polocloud.api;

import dev.httpmarco.osgan.networking.ClassSupplier;
import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.players.ClusterPlayerProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class CloudAPI {

    @Getter
    private static CloudAPI instance;

    public CloudAPI() {
        instance = this;
    }

    public abstract ClusterServiceProvider serviceProvider();

    public abstract ClusterGroupProvider groupProvider();

    public abstract EventProvider eventProvider();

    public abstract ClusterPlayerProvider playerProvider();

    public abstract ClassSupplier classSupplier();

    public abstract void classSupplier(ClassSupplier classSupplier);

    public Class<?> classByName(String name) throws ClassNotFoundException {
        if (classSupplier() == null) {
            throw new NullPointerException("The classSupplier on this instance is null! This means you either don't have the PoloCloud plugin installed or your main class does not call CommunicationClient#classLoader()");
        }

        return classSupplier().classByName(name);
    }
}
