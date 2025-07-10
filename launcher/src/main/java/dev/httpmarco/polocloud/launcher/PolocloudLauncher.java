package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.dependencies.DependencyProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class PolocloudLauncher {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var ownPath = Paths.get(PolocloudProcess.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        // we need to load the current version from the manifest data
        String version = PolocloudParameters.readManifest(PolocloudParameters.VERSION_ENV_ID, ownPath);
        if (version != null) {
            System.setProperty(PolocloudParameters.VERSION_ENV_ID, version);
        }

        Files.createDirectories(PolocloudParameters.LIB_DIRECTORY);

        // start the main context of the polocloud agent
        new PolocloudProcess().start();
    }
}
