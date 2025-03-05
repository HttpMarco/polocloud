package dev.httpmarco.polocloud.suite.cluster.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public final class ClusterSuiteInfoSnapshot {

    // date of creation
    private final long creationTime;
    // date of last update
    private long lastUpdateTime;

}
