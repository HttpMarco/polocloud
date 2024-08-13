package dev.httpmarco.polocloud.api.event.impl.services;

import dev.httpmarco.polocloud.api.event.common.ServiceEvent;
import dev.httpmarco.polocloud.api.services.ClusterService;

public final class ServiceStoppingEvent extends ServiceEvent {

    public ServiceStoppingEvent(ClusterService service) {
        super(service);
    }
}
