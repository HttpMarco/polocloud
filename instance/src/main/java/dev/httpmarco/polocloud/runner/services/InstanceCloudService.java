package dev.httpmarco.polocloud.runner.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public final class InstanceCloudService implements CloudService {

    private final int orderedId;
    private final UUID id;
    private final int port;
    private final ServiceState state;
    private CloudGroup group;


    @Override
    public List<String> log() {
        //todo
        return List.of();
    }
}
