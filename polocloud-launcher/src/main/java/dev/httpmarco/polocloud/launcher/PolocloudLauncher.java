package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.utils.FileCopyBuilder;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PolocloudLauncher {

    private static final Path LIB_FOLDER = Paths.get("dependencies");

    @SneakyThrows
    public PolocloudLauncher(String version) {
        this.registerShutdownHandling().cloneClasspathJars(version);

        // start main thread of the polocloud node
        new PolocloudProcess().start();
    }

    public PolocloudLauncher registerShutdownHandling() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {}));
        return this;
    }

    public void cloneClasspathJars(String version) {
        FileCopyBuilder.builder()
                .targetFolder(LIB_FOLDER.toString())
                .prefix("polocloud-")
                .files("api", "node", "daemon", "common")
                .suffix("-" + version + ".jar")
                .copy();
    }
}