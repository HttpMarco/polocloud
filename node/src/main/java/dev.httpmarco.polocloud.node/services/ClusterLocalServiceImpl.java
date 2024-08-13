package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.osgan.networking.channel.ChannelTransmit;
import dev.httpmarco.polocloud.api.event.EventPoolRegister;
import dev.httpmarco.polocloud.api.event.EventSubscribePool;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.util.DirectoryActions;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
public final class ClusterLocalServiceImpl extends ClusterServiceImpl {

    private @Nullable Process process;
    private @Nullable Thread processTracking;
    private @Nullable ChannelTransmit transmit;

    private final Path runningDir;
    private final List<String> logs = new ArrayList<>();

    private final EventSubscribePool eventSubscribePool = new EventSubscribePool(this.name());

    public ClusterLocalServiceImpl(ClusterGroup group, int orderedId, UUID id, int port, String hostname, String runningNode) {
        super(group, orderedId, id, port, hostname, runningNode);

        this.runningDir = Path.of("running/" + name() + "-" + id);
        this.runningDir.toFile().mkdirs();
    }

    @Override
    public void shutdown() {
        Node.instance().serviceProvider().factory().shutdownGroupService(this);
    }

    @Override
    @SneakyThrows
    public @NotNull List<String> logs() {
        var inputStream = this.process.getInputStream();
        var bytes = new byte[2048];
        int length;
        while (inputStream.available() > 0 && (length = inputStream.read(bytes, 0, bytes.length)) != -1) {
            logs.addAll(Arrays.asList(new String(bytes, 0, length, StandardCharsets.UTF_8).split("\n")));
        }
        return logs;
    }

    @SneakyThrows
    public void start(@NotNull ProcessBuilder builder) {
        builder.redirectError(new File("Test" + id()));
        this.process = builder.start();

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
                this.postShutdownProcess();
            }
        });

        this.processTracking.start();
    }

    @Override
    @SneakyThrows
    public void executeCommand(String command) {
        if (!hasProcess() || command == null || command.isEmpty()) {
            return;
        }
        var writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    public void destroyService() {
        if (process != null) {
            state(ClusterServiceState.STOPPING);
            this.process.toHandle().destroyForcibly();
            this.postShutdownProcess();
        }
    }

    @SneakyThrows
    public void postShutdownProcess() {
        this.process = null;

        if (this.processTracking != null) {
            this.processTracking.interrupt();
            this.processTracking = null;
        }

        synchronized (this) {
            DirectoryActions.delete(runningDir);
            Files.deleteIfExists(runningDir);
        }

        // unregister event pool
        EventPoolRegister.remove(eventSubscribePool);

        log.info("The service &8'&f{}&8' &7is stopped now&8!", name());
        Node.instance().serviceProvider().services().remove(this);
    }

    public boolean hasProcess() {
        return this.process != null;
    }
}
