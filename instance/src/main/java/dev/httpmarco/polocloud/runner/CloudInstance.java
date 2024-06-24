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

package dev.httpmarco.polocloud.runner;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.dependencies.Dependency;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.packets.service.CloudServiceMaxPlayersUpdatePacket;
import dev.httpmarco.polocloud.api.player.CloudPlayerProvider;
import dev.httpmarco.polocloud.api.properties.CloudProperty;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import dev.httpmarco.polocloud.runner.event.InstanceGlobalEventNode;
import dev.httpmarco.polocloud.runner.groups.InstanceGroupProvider;
import dev.httpmarco.polocloud.runner.player.InstanceCloudPlayerProvider;
import dev.httpmarco.polocloud.runner.services.InstanceServiceProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;
import java.util.jar.JarFile;

@Getter
@Accessors(fluent = true)
public class CloudInstance extends CloudAPI {

    public static final UUID SELF_ID = UUID.fromString(System.getenv("serviceId"));

    public static void main(String[] args) {
        //start platform
        new CloudInstance(args);
    }

    @Getter
    private static CloudInstance instance;

    private final CloudInstanceClient client;
    private final CloudGroupProvider groupProvider = new InstanceGroupProvider();
    private final CloudServiceProvider serviceProvider = new InstanceServiceProvider();
    private final CloudPlayerProvider playerProvider = new InstanceCloudPlayerProvider();
    private final InstanceGlobalEventNode globalEventNode;

    private CloudService self;

    @SneakyThrows
    public CloudInstance(String[] args) {
        instance = this;

        Dependency.load("net.kyori", "adventure-text-minimessage", "4.17.0");
        Dependency.load("net.kyori", "adventure-key", "4.17.0");
        Dependency.load("net.kyori", "examination-api", "1.3.0");
        Dependency.load("net.kyori", "adventure-api", "4.17.0");
        Dependency.load("net.kyori", "adventure-platform-api", "4.3.3");
        Dependency.load("net.kyori", "adventure-platform-facet", "4.3.3");
        Dependency.load("net.kyori", "adventure-platform-bungeecord", "4.3.3");
        Dependency.load("net.kyori", "adventure-text-serializer-bungeecord", "4.3.2");
        Dependency.load("net.kyori", "adventure-text-serializer-legacy", "4.13.1");
        Dependency.load("net.kyori", "adventure-text-serializer-gson", "4.13.1");

        var bootstrapPath = Path.of(System.getenv("bootstrapFile") + ".jar");

        this.client = new CloudInstanceClient("127.0.0.1", 8192, () -> serviceProvider.findAsync(SELF_ID).whenComplete((service, throwable) -> this.self = service));

        this.client.transmitter().listen(CloudServiceMaxPlayersUpdatePacket.class, (channel, packet) -> {
            if (self().id().equals(packet.id())) {
                self().maxPlayers(packet.maxPlayers());
            }
        });

        RunnerBootstrap.LOADER.addURL(bootstrapPath.toUri().toURL());

        this.globalEventNode = new InstanceGlobalEventNode();

        final var thread = new Thread(() -> {

            try (final var jar = new JarFile(bootstrapPath.toFile())) {

                if (Boolean.parseBoolean(System.getenv("appendSearchClasspath"))) {
                    RunnerBootstrap.INSTRUMENTATION.appendToSystemClassLoaderSearch(jar);
                }

                final var mainClass = jar.getManifest().getMainAttributes().getValue("Main-Class");
                try {
                    final var main = Class.forName(mainClass, true, RunnerBootstrap.LOADER).getMethod("main", String[].class);

                    main.invoke(null, (Object) Arrays.copyOfRange(args, 1, args.length));
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setContextClassLoader(RunnerBootstrap.LOADER);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (thread.isAlive()) {
                thread.interrupt();
            }
            this.client.transmitter().close();
        }));
        thread.start();
    }

    @Override
    public NodeService nodeService() {
        //todo
        return null;
    }

    @Override
    public PropertiesPool<CloudProperty<?>> globalProperties() {
        //todo
        return null;
    }
}
