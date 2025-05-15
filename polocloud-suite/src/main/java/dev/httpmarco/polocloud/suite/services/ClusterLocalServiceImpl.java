package dev.httpmarco.polocloud.suite.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.explanation.event.EventServiceGrpc;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import io.grpc.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@Getter
@Setter
@Accessors(fluent = true)
public final class ClusterLocalServiceImpl extends ClusterServiceImpl implements ClusterLocalService {

    private final List<String> logs = new CopyOnWriteArrayList<>();
    private Path path;

    private Process process;
    private @Nullable Thread processTracking;

    private Channel channel;

    // grpc channel stubs
    private EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub;

    public ClusterLocalServiceImpl(int id, int port, UUID uniqueId, ClusterGroup group) {
        super(id, uniqueId, group, "127.0.0.1", port,  ClusterServiceState.PREPARE);
    }

    @Override
    public void shutdown() {
        if(this.processTracking != null) {
            this.processTracking.interrupt();
        }
        changeState(ClusterServiceState.STOPPING);

        PolocloudSuite.instance().serviceProvider().factory().shutdownInstance(this);
    }

    @Override
    public boolean executeCommand(String command) {
        if (process == null || command == null || command.isEmpty()) {
            log.warn(PolocloudSuite.instance().translation().get("suite.cluster.localService.commandNotExecuted"));
            return false;
        }

        try {
            var writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
            writer.write(command);
            writer.newLine();
            writer.flush();
            return true;
        } catch (IOException e) {
            log.debug(PolocloudSuite.instance().translation().get("suite.cluster.localService.commandExecutionFailed", command), e);
            return false;
        }
    }

    public void startTracking() {
        // start process tracking
        this.processTracking = new Thread(() -> {
            // if player send a stop command from game command system
            try {
                this.process.waitFor();
            } catch (InterruptedException ignore) {
            }

            if (state() != ClusterServiceState.STOPPING) {
                state(ClusterServiceState.STOPPING);
                if (process != null) {
                    this.process.exitValue();
                }
                PolocloudSuite.instance().serviceProvider().factory().shutdownInstance(this);
            }
        });
        this.processTracking.start();
    }

    public void changeState(ClusterServiceState state) {
        super.state(state);
    }

    public void channel(Channel channel) {
        this.channel = channel;

        this.eventServiceBlockingStub = EventServiceGrpc.newBlockingStub(channel);
    }
}