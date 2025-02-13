package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PolocloudSuite extends Polocloud {

    private static final Logger log = LogManager.getLogger(PolocloudSuite.class);

    public PolocloudSuite() {
        // todo start context
        log.info("Initializing Polocloud Suite");
    }
}