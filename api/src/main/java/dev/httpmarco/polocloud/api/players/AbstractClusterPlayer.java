package dev.httpmarco.polocloud.api.players;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class AbstractClusterPlayer implements ClusterPlayer {

    private String name;
    private UUID uniqueId;
    private String currentServerName;
    private String currentProxyName;

}
