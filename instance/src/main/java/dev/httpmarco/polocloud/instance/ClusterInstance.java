package dev.httpmarco.polocloud.instance;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.api.services.ClusterServiceProvider;
import dev.httpmarco.polocloud.launcher.PoloCloudLauncher;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;

public final class ClusterInstance extends CloudAPI {

    @SneakyThrows
    public ClusterInstance(String[] args) {
        var bootPlatformFile = System.getenv("bootstrapFile");

        File file = Path.of(bootPlatformFile).toFile();
        PoloCloudLauncher.CLASS_LOADER.addURL(file.toURI().toURL());

        final var thread = new Thread(() -> {
            try (final var jar = new JarFile(file)) {

                final var mainClass = jar.getManifest().getMainAttributes().getValue("Main-Class");
                try {
                    var main = Class.forName(mainClass, true, PoloCloudLauncher.CLASS_LOADER).getMethod("main", String[].class);
                    var platformArgs = Arrays.stream(args).filter(it -> !it.equalsIgnoreCase("--instance")).toArray(String[]::new);

                    main.invoke(null, (Object) platformArgs);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setContextClassLoader(PoloCloudLauncher.CLASS_LOADER);
        thread.start();
    }

    @Override
    public ClusterServiceProvider serviceProvider() {
        //todo
        return null;
    }

    @Override
    public ClusterGroupProvider groupProvider() {
        //todo
        return null;
    }
}
