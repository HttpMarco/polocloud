package dev.httpmarco.polocloud.api.services.events;

import dev.httpmarco.polocloud.api.services.ClusterService;

public final class ClusterServiceStopEvent extends ClusterServiceEvent {

    public ClusterServiceStopEvent(ClusterService service) {
        super(service);
    }
}
