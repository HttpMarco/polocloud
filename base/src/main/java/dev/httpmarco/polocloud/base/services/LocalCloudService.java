package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.ServiceState;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class LocalCloudService extends CloudServiceImpl {

    private final Path runningFolder;

    @Setter
    private Process process;

    public LocalCloudService(CloudGroup group, int orderedId, UUID id, int port, ServiceState state) {
        super(group, orderedId, id, port, state);

        this.runningFolder = Path.of("running/" + name() + "-" + id());
    }


    @Override
    @SneakyThrows
    public List<String> log() {
        var logs = new ArrayList<String>();
        var inputStream = process.getInputStream();
        var bytes = new byte[2048];
        int length;
        while (inputStream.available() > 0 && (length = inputStream.read(bytes, 0, bytes.length)) != -1) {
            logs.addAll(Arrays.stream(new String(bytes, 0, length, StandardCharsets.UTF_8).split("\n")).toList());
        }
        return logs;
    }
}
