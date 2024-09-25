package dev.httpmarco.polocloud.api.event.impl.services.log;

import dev.httpmarco.polocloud.api.event.common.ServiceEvent;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

/*
 * Gets called when a service logs a new message
 */
@Getter
@Accessors(fluent = true)
public class ServiceLogEvent extends ServiceEvent {

    private final List<String> newLogs;

    public ServiceLogEvent(ClusterService service, List<String> newLogs) {
        super(service);

        this.newLogs = newLogs;
    }
}
