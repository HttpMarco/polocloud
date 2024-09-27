package dev.httpmarco.polocloud.launcher.util;

import java.util.jar.Manifest;

public class VersionUtil {

    public boolean checkVersion() {
        try (var inputStream = VersionUtil.class.getResourceAsStream("/META-INF/MANIFEST.MF")) {
            if (inputStream != null) {
                var manifest = new Manifest(inputStream);
                var attribut = manifest.getMainAttributes();
                var version = attribut.getValue("Polocloud-Version");

                if (version != null) {
                    System.setProperty("Polocloud-Version", version);
                    return true;
                }

                System.err.println("Version information not available");
                return false;
            }

            System.err.println("Version information not available");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}