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
        var apiPath = processFile("api");
        var bootPath =  processFile("node");

        // pre-process - copy runtime file
        ClasspathUtils.copyClassPathFile(apiPath.getFileName().toString(), apiPath.toString());
        ClasspathUtils.copyClassPathFile(bootPath.getFileName().toString(), bootPath.toString());

        // we start this as a new process, because we want to keep the launcher running and can update the cloud
        // also redirect output inheritIO
        var processBuilder = new ProcessBuilder("java", "-cp", bootPath + ";" + apiPath, "-javaagent:dependencies/polocloud-node-" + PoloCloud.version() + ".jar", ManifestReader.detectMainClass(bootPath.toFile())).inheritIO();
        var process = processBuilder.start();

        process.waitFor();
    }

    @Contract(pure = true)
    private @NotNull Path processFile(String filePart) {
        return RUNTIME_PATH.resolve( "polocloud-" + filePart + "-" + PoloCloud.version() + ".jar");
    }
}
