package dev.httpmarco.polocloud.node.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.jar.JarFile;

@UtilityClass
public class ManifestReader {

    @SneakyThrows
    public String detectVersion() {
        var path = ManifestReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        try (JarFile jarFile = new JarFile(path)) {
            return jarFile.getManifest().getMainAttributes().getValue("Polocloud-Version");
        }
    }
}
