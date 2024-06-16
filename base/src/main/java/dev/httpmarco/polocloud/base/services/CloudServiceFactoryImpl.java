/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.base.services;

import dev.httpmarco.osgan.files.OsganFile;
import dev.httpmarco.osgan.files.OsganFileCreateOption;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.events.service.CloudServiceShutdownEvent;
import dev.httpmarco.polocloud.api.events.service.CloudServiceStartEvent;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.GroupProperties;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceFactory;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.polocloud.base.CloudBase;
import dev.httpmarco.polocloud.base.groups.CloudGroupPlatformService;
import dev.httpmarco.polocloud.base.groups.platforms.PaperPlatform;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
public final class CloudServiceFactoryImpl implements CloudServiceFactory {

    // todo not used
    private static final Path RUNNING_FOLDER = OsganFile.define("running", OsganFileCreateOption.CREATION).path();

    @Override
    @SneakyThrows
    public void start(CloudGroup cloudGroup) {
        var service = new LocalCloudService(cloudGroup, this.nextServiceId(cloudGroup), UUID.randomUUID(), ServicePortDetector.detectServicePort(cloudGroup), ServiceState.PREPARED);
        ((CloudServiceProviderImpl) CloudAPI.instance().serviceProvider()).registerService(service);

        CloudAPI.instance().logger().info("Server " + service.name() + " is starting now on node " + CloudAPI.instance().nodeService().localNode().name() + "&2.");

        // download and/or copy platform file to service
        CloudGroupPlatformService platformService = CloudBase.instance().groupProvider().platformService();
        platformService.preparePlatform(service);

        var args = new LinkedList<>();

        args.add("java");

        // default commands
        args.add("-Djline.terminal=jline.UnsupportedTerminal");

        //todo better
        args.addAll(Arrays.stream(platformService.find(cloudGroup.platform().version()).platformsEnvironment()).toList());
        args.add("-javaagent:../../polocloud.jar");
        args.add("-jar");
        args.add("../../polocloud.jar");
        args.add("--instance");
        args.addAll(Arrays.stream(platformService.find(cloudGroup.platform().version()).platformsArguments()).toList());

        var processBuilder = new ProcessBuilder().directory(service.runningFolder().toFile()).command(args.toArray(String[]::new));

        //todo better
        processBuilder.environment().put("hostname", service.hostname());
        processBuilder.environment().put("port", String.valueOf(service.port()));
        processBuilder.environment().put("appendSearchClasspath", String.valueOf(!(platformService.find(service.group().platform().version()) instanceof PaperPlatform)));
        processBuilder.environment().put("bootstrapFile", service.group().platform().version());
        processBuilder.environment().put("serviceId", service.id().toString());
        processBuilder.environment().put("proxySecret", CloudGroupPlatformService.PROXY_SECRET);

        CloudBase.instance().templatesService().cloneTemplate(service);

        var pluginDirectory = service.runningFolder().resolve("plugins");

        OsganFile.create(pluginDirectory.toString());

        // copy polocloud plugin
        //todo a better way for minestom
        java.nio.file.Files.copy(Path.of("local/polocloud-plugin.jar"), pluginDirectory.resolve("polocloud-plugin.jar"), StandardCopyOption.REPLACE_EXISTING);

        service.state(ServiceState.STARTING);
        CloudAPI.instance().globalEventNode().call(new CloudServiceStartEvent(service));

        service.process(processBuilder.start());

        new Thread(() -> {
            try {
                service.process().waitFor();

                CloudAPI.instance().globalEventNode().call(new CloudServiceShutdownEvent(service));

                if (!service.group().properties().has(GroupProperties.STATIC)) {
                    synchronized (this) {
                        FileUtils.deleteDirectory(service.runningFolder().toFile());
                        java.nio.file.Files.deleteIfExists(service.runningFolder());
                    }
                }

                ((CloudServiceProviderImpl) CloudAPI.instance().serviceProvider()).unregisterService(service);
                CloudAPI.instance().logger().info("Server " + service.name() + " is stopped now.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    @SneakyThrows
    public void stop(CloudService service) {
        if (!(service instanceof LocalCloudService localCloudService)) {
            return;
        }

        if (localCloudService.process() != null) {
            // todo fix
            if (localCloudService.group().platform().proxy()) {
                localCloudService.execute("end");
            } else {
                localCloudService.execute("stop");
            }
            this.shutdownProcess(localCloudService);
        }
    }

    @SneakyThrows
    private void shutdownProcess(LocalCloudService service) {
        service.process().toHandle().destroyForcibly();
        service.process().waitFor();
        service.process(null);
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

