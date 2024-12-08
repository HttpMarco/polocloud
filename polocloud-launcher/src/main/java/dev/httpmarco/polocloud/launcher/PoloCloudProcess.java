package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.utils.ClasspathUtils;
import dev.httpmarco.polocloud.launcher.utils.ManifestReader;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class PoloCloudProcess extends Thread {

    private static final Path RUNTIME_PATH = Path.of("dependencies");

    public PoloCloudProcess() {
        setDaemon(false);
    }

    @Override
    @SneakyThrows
    public void run() {
        var bootPath = RUNTIME_PATH.resolve(processFile());

        // pre-process - copy runtime file
        ClasspathUtils.copyClassPathFile(processFile(), bootPath.toString());

        // we start this as a new process, because we want to keep the launcher running and can update the cloud
        // also redirect output inheritIO
        var processBuilder = new ProcessBuilder("java", "-cp", bootPath.toString(), ManifestReader.detectMainClass(bootPath.toFile())).inheritIO();
        processBuilder.start();
    }

    @Contract(pure = true)
    private @NotNull String processFile() {
        return "polocloud-node-" + PoloCloud.version() + ".jar";
    }
}
