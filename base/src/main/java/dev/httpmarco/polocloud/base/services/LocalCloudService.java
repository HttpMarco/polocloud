package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class LocalCloudService extends CloudServiceImpl {

    private final Path runningFolder;

    @Setter
    private Process process;

    public LocalCloudService(CloudGroup group, int orderedId, UUID id) {
        super(group, orderedId, id);

        this.runningFolder = Path.of("running/" + name() + "-" + id());
    }

    @Override
    public List<String> log() {
        var logs = new ArrayList<String>();
        try (var standardOutput = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = standardOutput.readLine()) != null) {
                CloudAPI.instance().logger().info(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }
}
