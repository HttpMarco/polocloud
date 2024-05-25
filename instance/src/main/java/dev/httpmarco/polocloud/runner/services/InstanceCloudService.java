package dev.httpmarco.polocloud.runner.services;

import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class InstanceCloudService implements CloudService {

    private final int orderedId;

    @Override
    public CloudGroup group() {
        return null;
    }

    @Override
    public UUID id() {
        return null;
    }

    @Override
    public int port() {
        return 0;
    }

    @Override
    public List<String> log() {
        return List.of();
    }
}
