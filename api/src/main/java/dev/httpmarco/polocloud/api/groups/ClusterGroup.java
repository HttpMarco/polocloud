package dev.httpmarco.polocloud.api.groups;

import dev.httpmarco.polocloud.api.Detail;
import dev.httpmarco.polocloud.api.Named;
import dev.httpmarco.polocloud.api.platforms.PlatformGroupDisplay;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.ClusterService;

import java.util.List;

public interface ClusterGroup extends Named, Detail {

    String[] nodes();

    String[] templates();

    int maxMemory();

    boolean staticService();

    PlatformGroupDisplay platform();

    int minOnlineServerInstances();

    int maxOnlineServerInstances();

    long serviceCount();

    List<ClusterService> services();

    PropertiesPool properties();

    default boolean fallback() {
        return this instanceof FallbackClusterGroup;
    }

}
