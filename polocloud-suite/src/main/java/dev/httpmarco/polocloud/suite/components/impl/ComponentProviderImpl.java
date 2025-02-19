package dev.httpmarco.polocloud.suite.components.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.suite.components.Component;
import dev.httpmarco.polocloud.suite.components.ComponentContainer;
import dev.httpmarco.polocloud.suite.components.ComponentInfoSnapshot;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;
import dev.httpmarco.polocloud.suite.components.loader.ComponentClassLoader;
import dev.httpmarco.polocloud.suite.utils.FileClassScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class ComponentProviderImpl implements ComponentProvider {

    private static final Path COMPONENTS_PATH = Paths.get("components");
    private static final Logger log = LogManager.getLogger(ComponentProviderImpl.class);
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

            if (file.isDirectory()) {
                // we only search jar files
                continue;
            }

            if (!file.getName().endsWith(".jar")) {
                continue;
            }

            try (var loader = new ComponentClassLoader(file)) {
                var classes = FileClassScanner.classes(file);

                var mainClass = classes.stream().filter(it -> {
                    var loadedClass = loader.read(it);
                    return loadedClass != null && loadedClass.getSuperclass() != null && loadedClass.getSuperclass().equals(Component.class) && loadedClass.isAnnotationPresent(Component.Info.class);
                }).map(loader::read).findFirst();

                if (mainClass.isPresent()) {
                    try {
                        var info = mainClass.get().getAnnotation(Component.Info.class);
                        var instance = (Component) mainClass.get().getConstructor().newInstance();
                        var snapshot = new ComponentInfoSnapshot(info.name(), Version.parse(info.version()));
                        var container = new ComponentContainer(instance, snapshot, new ComponentClassLoader(file));

                        // successfully loaded
                        this.containers.add(container);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        log.error("Failed to instantiate component", e);
                    }
                }
            } catch (IOException e) {
                log.error("Failed to scan file", e);
            }
        }
        log.info("Loading {} components", containers.size());

        // initialize all components
        for (var container : this.containers) {
            container.component().start();
        }
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


    @Override
    public void close() {
        for (var container : this.containers) {
            container.component().stop();
        }
    }
}

