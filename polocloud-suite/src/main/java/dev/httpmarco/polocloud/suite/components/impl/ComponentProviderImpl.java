package dev.httpmarco.polocloud.suite.components.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.component.api.Component;
import dev.httpmarco.polocloud.component.api.ComponentSuite;
import dev.httpmarco.polocloud.component.api.system.SuiteSystemProvider;
import dev.httpmarco.polocloud.suite.PolocloudSuite;
import dev.httpmarco.polocloud.suite.components.ComponentContainer;
import dev.httpmarco.polocloud.suite.components.ComponentInfoSnapshot;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;
import dev.httpmarco.polocloud.suite.components.loader.ComponentClassLoader;
import dev.httpmarco.polocloud.suite.utils.FileClassScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class ComponentProviderImpl extends ComponentSuite implements ComponentProvider {

    private static final Path COMPONENTS_PATH = Paths.get("components");
    private static final Logger log = LogManager.getLogger(ComponentProviderImpl.class);
    private final List<ComponentContainer> containers = new ArrayList<>();

    static {
        try {
            Files.createDirectories(COMPONENTS_PATH);
        } catch (IOException e) {
            log.error(PolocloudSuite.instance().translation().get("suite.directories.create.error"), e);
        }
    }

    @Override
    public SuiteSystemProvider suiteSystemProvider() {
        return PolocloudSuite.instance();
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

                var mainClasses = classes.stream().filter(it -> {
                    var loadedClass = loader.read(it);
                    return loadedClass != null && loadedClass.getSuperclass() != null && loadedClass.getSuperclass().equals(Component.class) && loadedClass.isAnnotationPresent(Component.Info.class);
                }).map(loader::read).toList();

                if (mainClasses.isEmpty()) {
                    log.error(PolocloudSuite.instance().translation().get("suite.loading.components.mainclass.notfound"));
                    continue;
                }

                if (mainClasses.size() > 1) {
                    log.error(PolocloudSuite.instance().translation().get("suite.loading.components.mainclass.multiple"));
                    continue;
                }

                try {
                    var mainClass = mainClasses.get(0);
                    var info = mainClass.getAnnotation(Component.Info.class);
                    var instance = (Component) mainClass.getConstructor().newInstance();
                    var snapshot = new ComponentInfoSnapshot(info.name(), Version.parse(info.version()));
                    var container = new ComponentContainer(instance, snapshot, new ComponentClassLoader(file));

                    // successfully loaded
                    this.containers.add(container);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    log.error(PolocloudSuite.instance().translation().get("suite.loading.components.instantiate.failed"), e);
                }
            } catch (IOException e) {
                log.error(PolocloudSuite.instance().translation().get("suite.loading.components.file.scan.failed"), e);
            }
        }
        log.info(PolocloudSuite.instance().translation().get("suite.loading.components", containers.size()));

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

