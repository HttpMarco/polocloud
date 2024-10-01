package dev.httpmarco.polocloud.launcher.update;

import dev.httpmarco.polocloud.launcher.util.OperatingSystem;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.util.List;

@UtilityClass
public class AutoUpdateInstaller {

    public static void installUpdate(File newFile) {
        UpdateShutdownController.shutdown();
        var operatingSystem = OperatingSystem.detectOS();

        startJarWithDelay(operatingSystem, newFile, 10);
        System.exit(0);
    }

    private void startJarWithDelay(OperatingSystem operatingSystem, File jarFile, int delaySeconds) {
        List<String> command = switch (operatingSystem) {
            case WINDOWS -> List.of("cmd", "/c", "start", "cmd", "/k", String.format("timeout /t %d && java -jar \"%s\"", delaySeconds, jarFile.getAbsolutePath()));
            case LINUX -> List.of("bash", "-c", String.format("sleep %d; java -jar \"%s\"", delaySeconds, jarFile.getAbsolutePath()));
            case UNIX -> List.of("sh", "-c", String.format("sleep %d; java -jar \"%s\"", delaySeconds, jarFile.getAbsolutePath()));
            case MAC -> List.of("osascript", "-e", String.format("delay %d; do shell script \"java -jar \\\"%s\\\"\"", delaySeconds, jarFile.getAbsolutePath()));
            default -> throw new UnsupportedOperationException("Unsupported OS: " + operatingSystem);
        };

        try {
            new ProcessBuilder(command)
                    .inheritIO()
                    .start();
        } catch (IOException e) {
            System.err.println("Failed to start new version: " + e.getMessage());
        }
    }
}
