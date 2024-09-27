package dev.httpmarco.polocloud.launcher.update;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Path;

@UtilityClass
public class AutoUpdateInstaller {

    private static final Path tempDir = Path.of("local/temp");

    public static void installUpdate(File oldFile, File newFile) {
        UpdateShutdownController.shutdown();


    }
}
