package dev.httpmarco.polocloud.instance.service;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public final class ClusterServiceInstance implements ClusterService {

    private int id;
    private UUID uniqueId;
    private ClusterServiceState state;
    private ClusterGroup group;

    @Override
    public boolean executeCommand(String command) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<String> logs(int lines) {
        return List.of();
    }
}
