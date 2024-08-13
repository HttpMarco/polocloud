package dev.httpmarco.polocloud.api.event.common;

import dev.httpmarco.polocloud.api.event.Event;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public abstract class GroupEvent implements Event {

    private final ClusterGroup group;

}
