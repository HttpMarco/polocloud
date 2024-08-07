package dev.httpmarco.polocloud.launcher.boot;

import dev.httpmarco.polocloud.launcher.util.FileSystemUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class InstanceBoot extends AbstractBoot {

    @Override
    public void dependencyLoading() {

    }

    @Override
    public @NotNull File bootFile() {
        var instancePath = Path.of("local/dependencies/polocloud-instance.jar");
        var pluginPath = Path.of("local/dependencies/polocloud-plugin.jar");

        if (!Files.exists(instancePath)) {
            // create path if not exists
            instancePath.toFile().getParentFile().mkdirs();

            FileSystemUtils.copyClassPathFile(this.getClass().getClassLoader(), "polocloud-instance.jar", instancePath.toString());
        }

        if (!Files.exists(pluginPath)) {
            // create path if not exists
            pluginPath.toFile().getParentFile().mkdirs();

            FileSystemUtils.copyClassPathFile(this.getClass().getClassLoader(), "polocloud-plugin.jar", pluginPath.toString());
        }

        return instancePath.toFile();
    }
}
