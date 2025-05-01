package dev.httpmarco.polocloud.suite.services.factory;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class LocalServiceFactory implements ServiceFactory {

    private static final Path FACTORY_DIR = Path.of("temp");

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
            var path = FACTORY_DIR.resolve(service.name() + "-" + service.uniqueId());
            Files.createDirectories(path);

            service.path(path);

            var processBuilder = new ProcessBuilder();

            // if users start with a custom java location
            var javaLocation = System.getProperty("java.home");

            processBuilder.inheritIO();
            processBuilder.command(javaLocation, "-jar", "", "");

            try {
                service.process(processBuilder.start());
            } catch (IOException e) {
                log.error("Failed to start service {}: {}", e.fillInStackTrace(), service.name());
                // todo try reboot with properties retry 3 times
                // destroy this service
            }

        }
    }

    @Override
    public void shutdownInstance(ClusterService clusterService) {
        if (clusterService instanceof ClusterLocalServiceImpl service) {

            log.info("Service &8'&f{}&8' &7stop process...", service.name());

            if (service.process() != null) {
                // todo call exit
                var platform = PolocloudSuite.instance().platformProvider().findSharedInstance(service.group().platform());
                // shutdown the process with the right command -> else use the default stop command
                service.executeCommand(platform == null ? "stop" : platform.shutdownCommand());

                try {
                    if (service.process().waitFor(PolocloudSuite.instance().config().local().processTerminationIdleSeconds(), TimeUnit.SECONDS)) {
                        service.process().exitValue();
                        service.process(null);
                    }
                } catch (InterruptedException exception) {
                    log.debug("Failed to wait for process termination");
                }

                // the process is running...
                if (service.process() != null) {
                    service.process().toHandle().destroyForcibly();
                }
            }

            log.info("Service &8'&f{}&8' &7stopped successfully&8.", service.name());
        } else {
            // todo other suite
        }
    }
}
