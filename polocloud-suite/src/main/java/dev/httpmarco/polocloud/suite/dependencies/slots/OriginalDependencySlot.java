package dev.httpmarco.polocloud.suite.dependencies.slots;

import dev.httpmarco.polocloud.suite.PolocloudContext;
import dev.httpmarco.polocloud.suite.dependencies.Dependency;
import dev.httpmarco.polocloud.suite.dependencies.DependencySlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class OriginalDependencySlot implements DependencySlot {

    private static final Logger log = LogManager.getLogger(OriginalDependencySlot.class);

    @Override
    public void append(Dependency dependency) {
        try {
            PolocloudContext.attachPath(dependency.file().toPath());
        } catch (IOException e) {
            log.error("Failed to attach path: {}", dependency.file().toPath());
            throw new RuntimeException(e);
        }
    }
}
