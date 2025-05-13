package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class ClusterServiceImpl implements ClusterService {

    private final int id;
    private final UUID uniqueId;
    private final ClusterGroup group;

    private final String hostname;
    private final int port;

    @Setter(AccessLevel.PROTECTED)
    private ClusterServiceState state;

    @Override
    public @NotNull String name() {
        return group.name() + "-" + id;
    }

    @Override
    public boolean executeCommand(String command) {
        //todo
        return false;
    }

    @Override
    public void shutdown() {
        // todo
    }

    @Override
    public List<String> logs(int lines) {
        // todo
        return List.of();
    }
}
