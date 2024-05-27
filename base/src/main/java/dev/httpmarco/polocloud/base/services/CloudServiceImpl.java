package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class CloudServiceImpl implements CloudService {

    private CloudGroup group;
    private int orderedId;
    private UUID id;
    private int port;

    @Setter(AccessLevel.PACKAGE)
    private ServiceState state;


    private final Set<String> subscribedEvents = new HashSet<>();

    @Override
    public List<String> log() {
        //todo
        return List.of();
    }

    @Override
    public String toString() {
        return "group=" + group +
                ", orderedId=" + orderedId +
                ", state=" + state +
                ", id=" + id;
    }
}
