package dev.httpmarco.polocloud.suite.services.factory;

import dev.httpmarco.polocloud.api.services.ClusterService;
import dev.httpmarco.polocloud.suite.services.ClusterLocalServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public final class LocalServiceFactory implements ServiceFactory {

    private static final Path FACTORY_DIR = Path.of("temp");

    @SneakyThrows
    public LocalServiceFactory() {
        if(!FACTORY_DIR.toFile().exists()) {
            Files.createDirectories(FACTORY_DIR);
        } else {
            // todo drop dir
        }
    }

    @SneakyThrows
    @Override
    public void bootInstance(@NotNull ClusterLocalServiceImpl service) {
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
