package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

@Log4j2
@Getter
@Setter
@Accessors(fluent = true)
public final class ClusterLocalServiceImpl extends ClusterServiceImpl implements ClusterLocalService {

    private Process process;
    private Path path;

    public ClusterLocalServiceImpl(int id, UUID uniqueId, ClusterGroup group) {
        super(id, uniqueId, group);
    }

    @Override
    public void shutdown() {
        PolocloudSuite.instance().serviceProvider().factory().shutdownInstance(this);
    }

    @Override
    public boolean executeCommand(String command) {
        if (process == null) {
            log.warn("Cannot execute this command, because the process is not running!");
            return false;
        }

        try {
            var output = this.process.getOutputStream();
            output.write((command + "\n").getBytes(StandardCharsets.UTF_8));
            output.flush();
            return true;
        } catch (IOException e) {
            log.debug("Cannot execute command: {}", command, e);
            return false;
        }
    }
}