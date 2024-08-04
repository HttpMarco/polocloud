package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public final class ClusterLocalServiceImpl extends ClusterServiceImpl {

    private Process process;
    private Thread processTracking;

    private final Path runningDir;

    public ClusterLocalServiceImpl(ClusterGroup group, int orderedId, UUID id, int port, String hostname, String runningNode) {
        super(group, orderedId, id, port, hostname, runningNode);

        this.runningDir = Path.of("running/" + name() + "-" + id);
        this.runningDir.toFile().mkdirs();
    }

    @Override
    public void shutdown() {
        if (process == null || !process.isAlive()) {
            return;
        }
        process.toHandle().destroyForcibly();
    }

    @SneakyThrows
    public void start(ProcessBuilder builder) {
        this.process = builder.start();
    }
}
