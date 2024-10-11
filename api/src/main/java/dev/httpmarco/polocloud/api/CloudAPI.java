package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.players.ClusterPlayerProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class CloudAPI {

    @Getter
    private static CloudAPI instance;
    @Setter(AccessLevel.PRIVATE)
    private ClassSupplier supplier;

    public CloudAPI() {
        instance = this;
    }

    public abstract ClusterServiceProvider serviceProvider();

    public abstract ClusterGroupProvider groupProvider();

    public abstract EventProvider eventProvider();

    public abstract ClusterPlayerProvider playerProvider();

    public static void classSupplier(ClassSupplier supplier) {
        if (instance != null && instance.supplier == null) {
            instance.supplier(supplier);
        }
    }

    public Class<?> classByName(String name) throws ClassNotFoundException {
        if (this.supplier == null) {
            throw new NullPointerException("The classSupplier on this instance is null! This means you either don't have the PoloCloud plugin installed or your main class does not call CloudAPI.classSupplier()");
        }

        return this.supplier.classByName(name);
    }
}
