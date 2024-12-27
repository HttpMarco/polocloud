package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.api.PolocloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.inject.Inject;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class Node extends PolocloudAPI {

    @Inject
    private ClusterGroupProvider groupProvider;



}
