package dev.httpmarco.polocloud.base.configuration;

import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.base.node.ExternalNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class CloudConfiguration {

    private final UUID clusterId;
    private final String clusterName;
    private final int maxMemory;

    private final ExternalNode[] externalNodes;
    private final PropertiesPool<CloudProperty<?>> properties = new PropertiesPool<>();

}
