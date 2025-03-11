package dev.httpmarco.polocloud.suite;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;

@UtilityClass
public class PolocloudShutdownHandler {

    private static final Logger log = LogManager.getLogger(PolocloudShutdownHandler.class);
    private boolean idleShutdown = false;

    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(PolocloudShutdownHandler::shutdown));
    }

    public void shutdown() {
        if(idleShutdown) {
            return;
        }
        idleShutdown = true;

        AnsiConsole.systemUninstall();
        log.info("Shutting down Polocloud Suite...");
        System.exit(-1);
    }
}
