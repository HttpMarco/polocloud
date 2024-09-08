package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.util.FileSystemUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public final class PoloCloudBoot {


    private static final Path DEPENDENCY_DIR = Path.of("local/dependencies");

    @SneakyThrows
    public @NotNull File bootFile() {

        this.copyBootFiles("api", "plugin", "instance", "node");

        return DEPENDENCY_DIR.resolve("polocloud-node.jar").toFile();
    }

    public String mainClass() {
        try (var jarFile = new JarFile(bootFile())) {
            var manifest = jarFile.getManifest();
            if (manifest != null) {
                var mainAttributes = manifest.getMainAttributes();
                return mainAttributes.getValue(Attributes.Name.MAIN_CLASS);
            } else {
                throw new RuntimeException(new NullPointerException("No main class detectable!"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void copyBootFile(String name) {
        FileSystemUtils.copyClassPathFile(ClassLoader.getSystemClassLoader(), "polocloud-" + name + ".jar", Path.of("local/dependencies/polocloud-" + name + ".jar").toString());
    }

    private void copyBootFiles(String @NotNull ... names) {
        for (var name : names) {
            this.copyBootFile(name);
        }
    }
}