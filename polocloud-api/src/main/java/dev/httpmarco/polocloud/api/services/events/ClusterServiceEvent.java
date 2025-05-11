package dev.httpmarco.polocloud.api.services.events;

import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class ClusterServiceEvent implements Event {

    private ClusterService service;

}
