package dev.httpmarco.polocloud.api;

import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class PolocloudAPI {

    @Getter
    private static PolocloudAPI instance;

    public PolocloudAPI() {
        instance = this;
    }

    public abstract ClusterGroupProvider groupProvider();

}
