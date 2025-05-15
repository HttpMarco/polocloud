package dev.httpmarco.polocloud.suite.services.factory;

import dev.httpmarco.polocloud.api.PolocloudEnvironment;
import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.api.services.ClusterServiceState;
import dev.httpmarco.polocloud.common.OS;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class LocalServiceFactory implements ServiceFactory {

    private static final String INSTANCE_PATH = "../../local/libs/polocloud-instance-2.0.0.jar";
    private static final String API_PATH = "../../local/libs/polocloud-api-2.0.0.jar";
    private static final String GRPC_PATH = "../../local/libs/polocloud-grpc-2.0.0.jar";
    private static final String INSTANCE_MAIN_CLASS;

    private static final Path FACTORY_DIR = Path.of("temp");


    static {
        // todo read dynamic
        INSTANCE_MAIN_CLASS = "dev.httpmarco.polocloud.instance.PolocloudInstanceBoot";
    }

    @SneakyThrows
    public LocalServiceFactory() {
        if (!FACTORY_DIR.toFile().exists()) {
            Files.createDirectories(FACTORY_DIR);
        } else {
            // todo drop dir
        }
    }

    @SneakyThrows
    @Override
    public void bootInstance(ClusterService clusterService) {
        if (clusterService instanceof ClusterLocalServiceImpl service) {
            service.changeState(ClusterServiceState.STARTING);

            var path = FACTORY_DIR.resolve(service.name() + "-" + service.uniqueId());

            Files.createDirectories(path);
            service.path(path);

            // copy all binded templates
            PolocloudSuite.instance().templateService().bindTemplates(service);

            var processBuilder = new ProcessBuilder().directory(service.path().toFile());

            // if users start with a custom java location
            var javaLocation = System.getProperty("java.home");

            var arguments = new ArrayList<String>();
            var dependencies = new ArrayList<String>();
            dependencies.add(API_PATH);
            dependencies.add(GRPC_PATH);
            dependencies.addAll(PolocloudSuite.instance().dependencyProvider().original().bindDependencies().stream()
                    .map(it -> "../../local/dependencies/" + it.file().getName())
                    .toList());

            arguments.add(javaLocation + "/bin/java");


            arguments.add("-Dterminal.jline=false");
            arguments.add("-Dfile.encoding=UTF-8");

            arguments.addAll(List.of("-Xms10G",
                    "-Xmx10G", "-XX:+UseG1GC",
                    "-XX:+ParallelRefProcEnabled",
                    "-XX:MaxGCPauseMillis=200",
                    "-XX:+UnlockExperimentalVMOptions",
                    "-XX:+DisableExplicitGC","-XX:+AlwaysPreTouch",
                    "-XX:G1NewSizePercent=30",
                    "-XX:G1MaxNewSizePercent=40",
                    "-XX:G1HeapRegionSize=8M",
                    "-XX:G1ReservePercent=20",
                    "-XX:G1HeapWastePercent=5",
                    "-XX:G1MixedGCCountTarget=4",
                    "-XX:InitiatingHeapOccupancyPercent=15",
                    "-XX:G1MixedGCLiveThresholdPercent=90",
                    "-XX:G1RSetUpdatingPauseTimePercent=5",
                    "-XX:SurvivorRatio=32",
                    "-XX:+PerfDisableSharedMem",
                    "-XX:MaxTenuringThreshold=1",
                    "-Dusing.aikars.flags=https://mcflags.emc.gs",
                    "-Daikars.new.flags=true"));

            arguments.add("--enable-native-access=ALL-UNNAMED");
            // java 24 drop a big warning here -> temp fix
            arguments.add("--sun-misc-unsafe-memory-access=allow");

            arguments.add("-Xms" + service.group().minMemory() + "M");
            arguments.add("-Xmx" + service.group().maxMemory() + "M");

            arguments.add("-javaagent:%s".formatted(INSTANCE_PATH));
            arguments.add("-cp");
            arguments.add(String.join(OS.detect().processSeparator(), dependencies));
            arguments.add(INSTANCE_MAIN_CLASS);

            var platformProvider = PolocloudSuite.instance().platformProvider();
            var platform = platformProvider.findPlatform(service.group().platform());
            var version = platformProvider.findPlatformVersion(service.group().platform());

            if (platform == null) {
                log.error(PolocloudSuite.instance().translation().get("suite.service.platformVersion.failed", service.name(), service.group().platform()));
                return;
            }

            arguments.addAll(platform.startArguments());

            Map<String, String> environment = processBuilder.environment();
            environment.put(PolocloudEnvironment.POLOCLOUD_SUITE_HOSTNAME.name(), "localhost");
            environment.put(PolocloudEnvironment.POLOCLOUD_SUITE_PORT.name(), String.valueOf(PolocloudSuite.instance().config().cluster().port()));
            environment.put(PolocloudEnvironment.POLOCLOUD_SUITE_PLATFORM_PATH.name(), platform.name() + "-" + version.version() + "-" + version.buildId() + ".jar");
            environment.put(PolocloudEnvironment.POLOCLOUD_SERVICE_ID.name(), service.uniqueId().toString());
            environment.put(PolocloudEnvironment.POLOCLOUD_SERVICE_PORT.name(), String.valueOf(service.port()));
            environment.put(PolocloudEnvironment.POLOCLOUD_SERVICE_SEPARAT_CLASSLOADER.name(), platform.separateClassLoader().toString());
            // download platform file
            platformProvider.factory().bindPlatform(service);

            try {
                service.process(processBuilder.command(arguments).redirectOutput(new File(service.name())).redirectError(new File(service.name() + "-error")).start());
                service.startTracking();
            } catch (IOException e) {
                log.error(PolocloudSuite.instance().translation().get("suite.service.start.failed", e.fillInStackTrace(), service.name()));
                // todo try reboot with properties retry 3 times
                // destroy this service
            }
        }
    }

    @Override
    public void shutdownInstance(ClusterService clusterService) {
        if (clusterService instanceof ClusterLocalServiceImpl service) {
            log.info(PolocloudSuite.instance().translation().get("suite.service.stop.process", service.name()));
            if (service.process() != null) {
                var platform = PolocloudSuite.instance().platformProvider().findSharedInstance(service.group().platform());
                // shutdown the process with the right command -> else use the default stop command
                if (service.executeCommand(platform == null ? "stop" : platform.shutdownCommand())) {
                    try {
                        if (service.process().waitFor(PolocloudSuite.instance().config().local().processTerminationIdleSeconds(), TimeUnit.SECONDS)) {
                            service.process().exitValue();
                            service.process(null);
                        }
                    } catch (InterruptedException exception) {
                        log.debug(PolocloudSuite.instance().translation().get("suite.service.stop.waitFailed"));
                    }
                }
                // the process is running...
                if (service.process() != null) {
                    service.process().toHandle().destroyForcibly();
                    service.process(null);
                }
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
            PathUtils.deleteDirectory(service.path().toFile());
            // clear service directory
            log.info(PolocloudSuite.instance().translation().get("suite.service.stop.success", service.name()));

            PolocloudSuite.instance().serviceProvider().storage().destroy(clusterService.name());
        } else {
            // todo other suite
        }
    }
}
