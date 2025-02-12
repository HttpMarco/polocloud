package dev.httpmarco.polocloud.launcher.boot;

import dev.httpmarco.polocloud.launcher.PolocloudLauncher;
import dev.httpmarco.polocloud.launcher.utils.Parameters;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public final class PolocloudBoot {

    public static void main(String[] args) throws URISyntaxException {
        // own path of the launcher file
        var ownPath = Path.of(PolocloudBoot.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        // we need to load the current version from the manifest data
        System.setProperty(Parameters.VERSION, Objects.requireNonNull(Parameters.read(Parameters.VERSION, ownPath)));

        // all fine -> start launching
        new PolocloudLauncher();
    }
}
