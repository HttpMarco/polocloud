package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.Closeable;
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
import java.util.concurrent.TimeUnit;

@Log4j2
@Getter
@Setter
@Accessors(fluent = true)
public final class ClusterLocalServiceImpl extends ClusterServiceImpl implements Closeable {

    private Process process;
    private Path path;

    public ClusterLocalServiceImpl(int id, UUID uniqueId, ClusterGroup group) {
        super(id, uniqueId, group);
    }

    @Override
    public void close() {
        if (this.process == null) {
            return;
        }

        // todo call exit

        this.executeCommand(PolocloudSuite.instance().platformProvider().findSharedInstance(group().platform()).shutdownCommand());

        try {
            if (this.process.waitFor(PolocloudSuite.instance().config().local().processTerminationIdleSeconds(), TimeUnit.SECONDS)) {
                this.process.exitValue();
                this.process = null;
            }
        }catch (InterruptedException exception) {
            log.debug("Failed to wait for process termination");
        }

        // the process is running...
        if(process != null) {
            this.process.toHandle().destroyForcibly();
        }
    }

    @Override
    public void executeCommand(String command) {
        if(process == null) {
            log.warn("Cannot execute this command, because the process is not running!");
            return;
        }

        try {
            var output = this.process.getOutputStream();
            output.write((command + "\n").getBytes(StandardCharsets.UTF_8));
            output.flush();
        } catch (IOException e) {
            log.error("cannot execute command: {}", command, e);
        }
    }
}
