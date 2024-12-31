package dev.httpmarco.polocloud.launcher.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

@UtilityClass
public class ManifestReader {

    @SneakyThrows
    public String readProperty(Path path, String key) {
        try (var jarFile = new JarFile(path.toString())) {
            return jarFile.getManifest().getMainAttributes().getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public String readLocalProperty(String key) {
        return readProperty(Paths.get(ManifestReader.class.getProtectionDomain().getCodeSource().getLocation().toURI()), key);
    }
}