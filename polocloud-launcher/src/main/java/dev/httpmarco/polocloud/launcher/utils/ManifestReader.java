package dev.httpmarco.polocloud.launcher.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.jar.JarFile;

@UtilityClass
public final class ManifestReader {

    @SneakyThrows
    public String detectMainClass(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            return jarFile.getManifest().getMainAttributes().getValue("Main-Class");
        }
    }

    @SneakyThrows
    public String detectVersion() {
        var path = dev.httpmarco.polocloud.launcher.utils.ManifestReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        try (JarFile jarFile = new JarFile(path)) {
            return jarFile.getManifest().getMainAttributes().getValue("Polocloud-Version");
        }
    }
}
