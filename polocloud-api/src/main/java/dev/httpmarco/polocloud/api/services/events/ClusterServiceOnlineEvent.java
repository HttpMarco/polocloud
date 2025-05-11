package dev.httpmarco.polocloud.api.services.events;

import dev.httpmarco.polocloud.api.services.ClusterService;

public final class ClusterServiceOnlineEvent extends ClusterServiceEvent {

    public ClusterServiceOnlineEvent(ClusterService service) {
        super(service);
    }
}
