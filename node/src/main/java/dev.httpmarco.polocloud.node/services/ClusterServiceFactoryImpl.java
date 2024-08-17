package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.event.impl.services.ServiceStartEvent;
import dev.httpmarco.polocloud.api.event.impl.services.ServiceStoppingEvent;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceFactory;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.packets.resources.services.ClusterSyncRegisterServicePacket;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.tasks.PlatformDownloadTask;
import dev.httpmarco.polocloud.node.services.util.ServicePortDetector;
import dev.httpmarco.polocloud.node.templates.TemplateFactory;
import dev.httpmarco.polocloud.node.util.DirectoryActions;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Log4j2
public final class ClusterServiceFactoryImpl implements ClusterServiceFactory {

    public static int PROCESS_TIMEOUT = 5;

    @Override
    @SneakyThrows
    public void runGroupService(ClusterGroup group) {
        var runningNode = Node.instance().clusterProvider().localNode().data();

        var localService = new ClusterLocalServiceImpl(group, generateOrderedId(group), UUID.randomUUID(), ServicePortDetector.detectServicePort(group), "0.0.0.0", runningNode.name());

        Node.instance().eventProvider().factory().call(new ServiceStartEvent(localService));

        log.info("The service &8'&f{}&8' &7is starting now&8...", localService.name());
        Node.instance().serviceProvider().services().add(localService);

        // call other nodes
        Node.instance().clusterProvider().broadcast(new ClusterSyncRegisterServicePacket(localService));

        TemplateFactory.cloneTemplate(localService);

        PlatformDownloadTask.download(group).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                log.warn(throwable.getMessage());
                return;
            }

            try {
                // run platform actions
                var platform = Node.instance().platformService().platform(group.platform().platform());

                var arguments = new ArrayList<>();
                var bootJarPath = System.getProperty("bootLauncher");

                arguments.add("java");
                arguments.add("-Xms" + localService.group().minMemory() + "m");
                arguments.add("-Xmx" + localService.group().maxMemory() + "m");
                arguments.addAll(List.of("-Djline.terminal=jline.UnsupportedTerminal", "-XX:+UseG1GC", "-XX:+ParallelRefProcEnabled", "-XX:MaxGCPauseMillis=200", "-XX:+UnlockExperimentalVMOptions", "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch", "-XX:G1NewSizePercent=30", "-XX:G1MaxNewSizePercent=40", "-XX:G1HeapRegionSize=8M", "-XX:G1ReservePercent=20", "-XX:G1HeapWastePercent=5", "-XX:G1MixedGCCountTarget=4", "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=90", "-XX:G1RSetUpdatingPauseTimePercent=5", "-XX:SurvivorRatio=32", "-XX:+PerfDisableSharedMem", "-XX:MaxTenuringThreshold=1", "-Dusing.aikars.flags=https://mcflags.emc.gs", "-Daikars.new.flags=true", "-XX:-UseAdaptiveSizePolicy", "-XX:CompileThreshold=100", "-Dio.netty.recycler.maxCapacity=0", "-Dio.netty.recycler.maxCapacity.default=0", "-Djline.terminal=jline.UnsupportedTerminal", "-Dfile.encoding=UTF-8", "-Dclient.encoding.override=UTF-8", "-DIReallyKnowWhatIAmDoingISwear=true"));
                arguments.add("-javaagent:" + bootJarPath);
                arguments.add("-jar");
                arguments.add(bootJarPath);

                if (platform != null) {
                    platform.actions().forEach(platformAction -> platformAction.run(localService));
                    arguments.addAll(Arrays.stream(platform.startArguments()).toList());
                }

                // mark process as a services
                arguments.add("--instance");

                //copy platform jar and maybe patch files
                DirectoryActions.copyDirectoryContents(Path.of("local/platforms/" + group.platform().platform() + "/" + group.platform().version()), localService.runningDir());

                // create process
                var processBuilder = new ProcessBuilder(arguments.toArray(String[]::new)).directory(localService.runningDir().toFile());

                // todo remove but the log stops if not present
                processBuilder.redirectOutput(localService.runningDir().resolve("polocloud_info_log.txt").toFile());
                processBuilder.redirectError(localService.runningDir().resolve("polocloud_error_log.txt").toFile());

                // send the platform boot jar
                processBuilder.environment().put("bootstrapFile", group.platform().platformJarName());
                processBuilder.environment().put("nodeEndPointPort", String.valueOf(Node.instance().clusterProvider().localNode().data().port()));
                processBuilder.environment().put("serviceId", localService.id().toString());

                // copy platform plugin for have a better control of service
                var pluginDir = localService.runningDir().resolve("plugins");
                pluginDir.toFile().mkdirs();

                Files.copy(Path.of("local/dependencies/polocloud-plugin.jar"), pluginDir.resolve("polocloud-plugin.jar"), StandardCopyOption.REPLACE_EXISTING);

                localService.state(ClusterServiceState.STARTING);
                localService.update();

                // run platform
                localService.start(processBuilder);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutdownGroupService(ClusterService clusterService) {
        if (clusterService instanceof ClusterLocalServiceImpl localService) {
            localService.state(ClusterServiceState.STOPPING);
            Node.instance().eventProvider().factory().call(new ServiceStoppingEvent(clusterService));

            if (localService.hasProcess()) {
                var platform = Node.instance().platformService().platform(localService.group().platform().platform());

                // try with platform command a clean shutdown
                localService.executeCommand(platform == null ? Platform.DEFAULT_SHUTDOWN_COMMAND : platform.shutdownCommand());

                try {
                    if (localService.process().waitFor(PROCESS_TIMEOUT, TimeUnit.SECONDS)) {
                        localService.process().exitValue();
                        localService.postShutdownProcess();
                        return;
                    }
                } catch (InterruptedException ignored) {
                }
                localService.process().toHandle().destroyForcibly();
            }
            localService.postShutdownProcess();
        }
    }

    private int generateOrderedId(ClusterGroup group) {
        return IntStream.iterate(1, i -> i + 1).filter(id -> !isIdPresent(group, id)).findFirst().orElseThrow();
    }

    private boolean isIdPresent(@NotNull ClusterGroup group, int id) {
        return group.services().stream().anyMatch(it -> it.orderedId() == id);
    }
}
