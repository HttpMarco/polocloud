package dev.httpmarco.polocloud.launcher.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.nio.file.Paths;
import java.util.jar.JarFile;

@UtilityClass
public class ManifestReader {

    @SneakyThrows
    public String readProperty(String key) {
        var jarUrl = ManifestReader.class.getProtectionDomain().getCodeSource().getLocation();
        var jarPath = Paths.get(jarUrl.toURI());

        try (var jarFile = new JarFile(jarPath.toString())) {
            return jarFile.getManifest().getMainAttributes().getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
