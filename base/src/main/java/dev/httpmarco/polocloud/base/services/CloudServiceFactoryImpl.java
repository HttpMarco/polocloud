package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.service.CloudServiceStartEvent;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.groups.CloudServiceGroupProvider;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public final class CloudServiceFactoryImpl implements CloudServiceFactory {

    private static final Path RUNNING_FOLDER = Path.of("running");

    public CloudServiceFactoryImpl() {
        Files.createDirectoryIfNotExists(RUNNING_FOLDER);
    }

    @Override
    @SneakyThrows
    public void start(CloudGroup cloudGroup) {
        var service = new LocalCloudService(cloudGroup, this.nextServiceId(cloudGroup), UUID.randomUUID(), ServicePortDetector.detectServicePort(cloudGroup), ServiceState.PREPARED);
        ((CloudServiceProviderImpl) CloudAPI.instance().serviceProvider()).registerService(service);

        CloudAPI.instance().logger().info("Server " + service.name() + " is starting now on node " + CloudAPI.instance().nodeService().localNode().name() + "&2.");

        Files.createDirectoryIfNotExists(service.runningFolder());

        // download and/or copy platform file to service
        CloudGroupPlatformService platformService = CloudBase.instance().groupProvider().platformService();
        platformService.preparePlatform(service);

        var args = new LinkedList<>();

        args.add("java");
        args.add("-javaagent:../../polocloud.jar");
        args.addAll(Arrays.stream(platformService.find(cloudGroup.platform()).platformsEnvironment()).toList());
        args.add("-jar");
        args.add("../../polocloud.jar");
        args.add("--instance");
        args.add("--bootstrap=" + service.group().platform());
        args.addAll(Arrays.stream(platformService.find(cloudGroup.platform()).platformsArguments()).toList());

        var processBuilder = new ProcessBuilder().directory(service.runningFolder().toFile()).command(args.toArray(String[]::new));

        processBuilder.environment().put("serviceId", service.id().toString());

        processBuilder.redirectError(new File("test.log"));

        if (cloudGroup.properties().has(GroupProperties.TEMPLATES)) {
            var temp = CloudBase.instance().templatesService().templates(cloudGroup.properties().property(GroupProperties.TEMPLATES));
            if (temp != null) {
                temp.copy(service);
            }
        }

        var pluginDirectory = service.runningFolder().resolve("plugins");

        Files.createDirectoryIfNotExists(pluginDirectory);

        // copy polocloud plugin
        java.nio.file.Files.copy(Path.of("local/polocloud-plugin.jar"), pluginDirectory.resolve("polocloud-plugin.jar"));

        service.state(ServiceState.STARTING);
        CloudAPI.instance().globalEventNode().call(new CloudServiceStartEvent(service));

        service.process(processBuilder.start());
    }

    @Override
    @SneakyThrows
    public void stop(CloudService service) {
        if (!(service instanceof LocalCloudService localCloudService)) {
            return;
        }

        if (localCloudService.process() != null) {
            localCloudService.process().toHandle().destroyForcibly();
            localCloudService.process().waitFor();
            localCloudService.process(null);
        }

        synchronized (this) {
            FileUtils.deleteDirectory(localCloudService.runningFolder().toFile());
        }

        java.nio.file.Files.deleteIfExists(localCloudService.runningFolder());
        ((CloudServiceProviderImpl) CloudAPI.instance().serviceProvider()).unregisterService(service);
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

