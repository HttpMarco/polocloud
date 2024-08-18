package dev.httpmarco.polocloud.launcher.boot;

import dev.httpmarco.polocloud.launcher.PoloCloudLauncher;
import dev.httpmarco.polocloud.launcher.util.FileSystemUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;

public final class InstanceBoot extends AbstractBoot {

    @Override
    public void dependencyLoading() {

    }

    @SneakyThrows
    @Override
    public @NotNull File bootFile() {
        var instancePath = Path.of("../../local/dependencies/polocloud-instance.jar");

        if (!Files.exists(instancePath)) {
            // create path if not exists
            instancePath.toFile().getParentFile().mkdirs();

            FileSystemUtils.copyClassPathFile(this.getClass().getClassLoader(), "polocloud-instance.jar", instancePath.toString());
        }

        if(PoloCloudLauncher.INSTRUMENTATION != null) {
            PoloCloudLauncher.INSTRUMENTATION.appendToSystemClassLoaderSearch(new JarFile(instancePath.toFile()));
        }

        return instancePath.toFile();
    }
}
