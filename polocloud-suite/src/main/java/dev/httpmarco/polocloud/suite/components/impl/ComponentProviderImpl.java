package dev.httpmarco.polocloud.suite.components.impl;

import dev.httpmarco.polocloud.suite.components.ComponentContainer;
import dev.httpmarco.polocloud.suite.components.ComponentInfoSnapshot;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class ComponentProviderImpl implements ComponentProvider {

    private static final Path COMPONENTS_PATH = Paths.get("components");
    private static final Logger log = LoggerFactory.getLogger(ComponentProviderImpl.class);
    private final List<ComponentContainer> containers = new ArrayList<>();

    static {
        try {
            Files.createDirectories(COMPONENTS_PATH);
        } catch (IOException e) {
            log.error("Failed to create directories", e);
        }
    }

    public ComponentProviderImpl() {
        // load all components
        for (var file : Objects.requireNonNull(COMPONENTS_PATH.toFile().listFiles())) {

            if(file.isDirectory()) {
                // we only search jar files
                continue;
            }

            if(!  file.getName().endsWith(".jar")) {
                continue;
            }
        }
        log.info("Loading {} components", containers.size());
    }

    @Override
    public Collection<ComponentContainer> containers() {
        return List.copyOf(containers);
    }

    @Override
    public ComponentContainer find(ComponentInfoSnapshot snapshot) {
        return this.containers.stream().filter(it -> it.snapshot().equals(snapshot)).findFirst().orElse(null);
    }

    @Override
    public ComponentContainer find(String name) {
        return this.containers.stream().filter(it -> it.snapshot().id().equals(name)).findFirst().orElse(null);
    }
}

