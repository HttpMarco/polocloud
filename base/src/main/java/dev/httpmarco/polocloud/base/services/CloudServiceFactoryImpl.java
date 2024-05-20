package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.UUID;

public final class CloudServiceFactoryImpl implements CloudServiceFactory {

    private static final Path RUNNING_FOLDER = Path.of("running");

    public CloudServiceFactoryImpl() {
        Files.createDirectoryIfNotExists(RUNNING_FOLDER);
    }

    @Override
    public void start(CloudGroup cloudGroup) {
        var service = new LocalCloudService(cloudGroup, 1, UUID.randomUUID());
        ((CloudServiceProviderImpl) CloudAPI.instance().serviceProvider()).registerService(service);

        CloudAPI.instance().logger().info("Server " + service.name() + " is starting now on node " + CloudAPI.instance().nodeService().localNode().name() + ".");

        Files.createDirectoryIfNotExists(service.runningFolder());

    }

    @Override
    @SneakyThrows
    public void stop(CloudService service) {
        if (!(service instanceof LocalCloudService localCloudService)) {
            return;
        }

        if (localCloudService.process() != null) {
            localCloudService.process().destroyForcibly();
        }

        java.nio.file.Files.deleteIfExists(localCloudService.runningFolder());
        CloudAPI.instance().logger().info("Server " + service.name() + " is stopped now.");
    }
}
