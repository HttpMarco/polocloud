package dev.httpmarco.polocloud.suite;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.fusesource.jansi.AnsiConsole;

@Log4j2
@UtilityClass
public class PolocloudShutdownHandler {

    private final String SHUTDOWN_HOOK = "polocloud-shutdown-hook";
    private boolean idleShutdown = false;

    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(PolocloudShutdownHandler::shutdown, SHUTDOWN_HOOK));
    }

    public void shutdown() {
        if (idleShutdown) {
            return;
        }
        idleShutdown = true;

        var cluster = PolocloudSuite.instance().cluster();
        var translation = PolocloudSuite.instance().translation();
        var serviceProvider = PolocloudSuite.instance().serviceProvider();

        // stop all services and interrupt queue
        if (serviceProvider != null) {
            serviceProvider.close();
        }

        if (cluster != null) {
            log.info(translation.get("shutdown.cluster"));
            cluster.close();
            log.info(translation.get("shutdown.clusterSuccessfully"));
        }

        AnsiConsole.systemUninstall();
        log.info(translation.get("shutdown.suite"));

        if (!Thread.currentThread().getName().equals(SHUTDOWN_HOOK)) {
            System.exit(-1);
        }
    }
}
