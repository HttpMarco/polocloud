package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.utils.ManifestReader;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class PolocloudProcess extends Thread {

    public PolocloudProcess() {
        setDaemon(false);
    }

    @Override
    @SneakyThrows
    public void run() {
        var processBuilder = new ProcessBuilder();
        processBuilder.inheritIO().command(arguments());

        var process = processBuilder.start();
        process.waitFor();
    }


    private @NotNull @Unmodifiable List<String> arguments() {
        var version = System.getProperty("version");

        var mainBootFile = Path.of("dependencies").resolve("polocloud-node-" + version + ".jar");
        var mainClass = ManifestReader.readProperty(mainBootFile, "Main-Class");

        return List.of("java", "-javaagent:" + mainBootFile,"-cp", String.join(";", modifyClasspathFiles("node", "common", "api")), mainClass);
    }

    private String @NotNull [] modifyClasspathFiles(String... libs) {
        return Arrays.stream(libs).map(it -> "dependencies/polocloud-" + it + "-" + System.getProperty("version") + ".jar").toArray(String[]::new);
    }
}