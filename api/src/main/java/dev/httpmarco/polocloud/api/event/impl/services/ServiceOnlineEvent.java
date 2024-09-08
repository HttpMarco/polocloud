package dev.httpmarco.polocloud.api.event.impl.services;

import dev.httpmarco.polocloud.api.event.common.ServiceEvent;
import dev.httpmarco.polocloud.api.services.ClusterService;

public final class ServiceOnlineEvent extends ServiceEvent {

    public ServiceOnlineEvent(ClusterService service) {
        super(service);
    }
}
