package dev.httpmarco.polocloud.launcher.boot;

import dev.httpmarco.polocloud.launcher.util.FileSystemUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public final class NodeBoot extends AbstractBoot {

    @Override
    public void dependencyLoading() {

    }

    @Override
    @SneakyThrows
    public @NotNull File bootFile() {
        // copy cluster api in classpath
        var apiFile = Path.of("local/dependencies/polocloud-api.jar");
        FileSystemUtils.copyClassPathFile(ClassLoader.getSystemClassLoader(), "polocloud-api.jar", apiFile.toString());

        // copy cluster plugin in classpath
        var pluginFile = Path.of("local/dependencies/polocloud-plugin.jar");
        FileSystemUtils.copyClassPathFile(ClassLoader.getSystemClassLoader(), "polocloud-plugin.jar", pluginFile.toString());

        var path = Path.of("local/dependencies/polocloud-node.jar");

        // create path if not exists
        path.toFile().getParentFile().mkdirs();

        FileSystemUtils.copyClassPathFile(this.getClass().getClassLoader(), "polocloud-node.jar", path.toString());
        return path.toFile();
    }
}