package dev.httpmarco.polocloud.node.services;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceFactory;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.packets.resources.services.ClusterSyncRegisterServicePacket;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.actions.PlatformAction;
import dev.httpmarco.polocloud.node.platforms.tasks.PlatformDownloadTask;
import dev.httpmarco.polocloud.node.services.util.ServicePortDetector;
import dev.httpmarco.polocloud.node.util.DirectoryActions;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Log4j2
public final class ClusterServiceFactoryImpl implements ClusterServiceFactory {

    public static int PROCESS_TIMEOUT = 5;

    @Override
    @SneakyThrows
    public void runGroupService(ClusterGroup group) {
        var runningNode = Node.instance().clusterService().localNode().data();

        var localService = new ClusterLocalServiceImpl(group, generateOrderedId(group), UUID.randomUUID(), ServicePortDetector.detectServicePort(), "0.0.0.0", runningNode.name());

        //todo alert to other nodes

        log.info("The service &8'&f{}&8' &7is starting now&8...", localService.name());
        Node.instance().serviceProvider().services().add(localService);

        // call other nodes
        Node.instance().clusterService().broadcast(new ClusterSyncRegisterServicePacket(localService));

        PlatformDownloadTask.download(group).whenComplete((unused, throwable) -> {

            if (throwable != null) {
                log.warn(throwable.getMessage());
                return;
            }

            // run platform actions
            var platform = Node.instance().platformService().platform(group.platform().platform());

            if (platform != null) {
                platform.actions().forEach(platformAction -> platformAction.run(localService));
            }

            //copy platform jar and maybe patch files
            DirectoryActions.copyDirectoryContents(Path.of("local/platforms/" + group.platform().platform() + "/" + group.platform().version()), localService.runningDir());

            // create process
            var processBuilder = new ProcessBuilder("java", "-jar", group.platform().platformJarName()).directory(localService.runningDir().toFile());

            // run platform
            localService.start(processBuilder);
        });
    }

    @Override
    public void shutdownGroupService(ClusterService clusterService) {
        if (clusterService instanceof ClusterLocalServiceImpl localService) {
            localService.state(ClusterServiceState.STOPPING);
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
