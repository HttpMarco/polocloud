package dev.httpmarco.polocloud.api.services.events;

import dev.httpmarco.polocloud.api.services.ClusterService;

public final class ClusterServiceStartEvent extends ClusterServiceEvent {

    public ClusterServiceStartEvent(ClusterService service) {
        super(service);
    }
}
