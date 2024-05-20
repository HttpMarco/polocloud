package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.groups.CloudServiceGroupProvider;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public final class CloudServiceFactoryImpl implements CloudServiceFactory {

    private static final Path RUNNING_FOLDER = Path.of("running");

    public CloudServiceFactoryImpl() {
        Files.createDirectoryIfNotExists(RUNNING_FOLDER);
    }

    @Override
    @SneakyThrows
    public void start(CloudGroup cloudGroup) {
        var service = new LocalCloudService(cloudGroup, this.nextServiceId(cloudGroup), UUID.randomUUID());
        ((CloudServiceProviderImpl) CloudAPI.instance().serviceProvider()).registerService(service);

        CloudAPI.instance().logger().info("Server " + service.name() + " is starting now on node " + CloudAPI.instance().nodeService().localNode().name() + ".");

        Files.createDirectoryIfNotExists(service.runningFolder());

        // download and/or copy platform file to service
        ((CloudServiceGroupProvider) CloudBase.instance().groupProvider()).platformService().preparePlatform(service);

        service.process(new ProcessBuilder().directory(service.runningFolder().toFile())
                .command("java", "-javaagent:../../polocloud.jar", "-jar", "../../polocloud.jar", "--instance")
                .redirectOutput(new File("test"))
                .redirectError(new File("test2"))
                .start());
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

    private int nextServiceId(CloudGroup cloudGroup) {
        int id = 1;
        while (this.isIdPresent(cloudGroup, id)) {
            id++;
        }
        return id;
    }

    private boolean isIdPresent(CloudGroup group, int id) {
        return CloudAPI.instance().serviceProvider().services(group).stream().anyMatch(it -> it.orderedId() == id);
    }
}
