package dev.httpmarco.polocloud.suite;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;

@UtilityClass
public class PolocloudShutdownHandler {

    private static final Logger log = LogManager.getLogger(PolocloudShutdownHandler.class);

    private final String SHUTDOWN_HOOK = "polocloud-shutdown-hook";
    private boolean idleShutdown = false;

    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(PolocloudShutdownHandler::shutdown, SHUTDOWN_HOOK));
    }

    public void shutdown() {
        if(idleShutdown) {
            return;
        }
        idleShutdown = true;

        var cluster = PolocloudSuite.instance().cluster();
        if(cluster != null) {
            log.info("Shutting down Cluster...");
            cluster.close();
            log.info("Cluster is successfully shut down.");
        }

        AnsiConsole.systemUninstall();
        log.info("Shutting down Polocloud Suite...");

        if(!Thread.currentThread().getName().equals(SHUTDOWN_HOOK)) {
            System.exit(-1);
        }
    }
}
