package dev.httpmarco.polocloud.suite.cluster.configuration;


import dev.httpmarco.polocloud.suite.cluster.ClusterConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class ClusterLocalConfig implements ClusterConfig {

    private final String id;
    private final int port;

}
