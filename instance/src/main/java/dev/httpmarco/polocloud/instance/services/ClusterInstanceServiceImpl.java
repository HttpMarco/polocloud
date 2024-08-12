package dev.httpmarco.polocloud.instance.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceCommandPacket;
import dev.httpmarco.polocloud.api.packet.resources.services.ServiceShutdownCallPacket;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.instance.ClusterInstance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public final class ClusterInstanceServiceImpl implements ClusterService {

    private final UUID id;
    private final int orderedId;
    private final int port;
    private final String hostname;
    private final String runningNode;
    private final ClusterServiceState state;
    private ClusterGroup group;

    @Override
    public void shutdown() {
        ClusterInstance.instance().client().sendPacket(new ServiceShutdownCallPacket(this.id));
    }

    @Override
    public void executeCommand(String command) {
        ClusterInstance.instance().client().sendPacket(new ServiceCommandPacket(id, command));
    }

    @Override
    public List<String> logs() {
        // todo
        return List.of();
    }

    @Override
    public void update() {
        //todo
    }

    @Override
    public String details() {
        return this.toString();
    }
}
