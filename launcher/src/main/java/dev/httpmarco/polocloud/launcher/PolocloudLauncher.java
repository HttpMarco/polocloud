package dev.httpmarco.polocloud.launcher;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class PolocloudLauncher {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var ownPath = Paths.get(PolocloudProcess.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        // we need to load the current version from the manifest data
        String version = PolocloudParameters.readManifest(PolocloudParameters.VERSION_ENV_ID, ownPath);
        if (version != null) {
            System.setProperty(PolocloudParameters.VERSION_ENV_ID, version);
        }

        Files.createDirectories(PolocloudParameters.LIB_DIRECTORY);

        List<String> argumentList = Arrays.stream(args).toList();
        var development = argumentList.contains("--development") || argumentList.contains("--dev");
        var process = new PolocloudProcess(development);
        process.start();
    }
}
