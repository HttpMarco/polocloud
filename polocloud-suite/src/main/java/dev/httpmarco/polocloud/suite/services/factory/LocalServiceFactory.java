package dev.httpmarco.polocloud.suite.services.factory;

import dev.httpmarco.polocloud.api.services.ClusterService;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public final class LocalServiceFactory implements ServiceFactory {

    @Override
    public void bootInstance(ClusterService service) {

        var processBuilder = new ProcessBuilder();

        // todo use different platforms
        processBuilder.command("", "", "", "");

        try {
            processBuilder.start();
        } catch (IOException e) {
            log.error("Failed to start service {}: {}", e.fillInStackTrace(), service.name());

            // todo try reboot with properties retry 3 times
            // destroy this service
        }
    }
}
