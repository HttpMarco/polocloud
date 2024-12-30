package dev.httpmarco.polocloud.launcher;

import dev.httpmarco.polocloud.launcher.utils.FileCopyBuilder;
import lombok.SneakyThrows;

public final class PolocloudProcess {

    @SneakyThrows
    public PolocloudProcess() {
       this.registerShutdownHandling()
                .cloneClasspathJars()
                .run();
    }

    /**
     * Polocloud process shutdown handler
     */
    public PolocloudProcess registerShutdownHandling() {
        //todo
        //  Runtime.getRuntime().addShutdownHook(new Thread(lock::publish));
        return this;
    }

    /**
     * We include all cloud jars in
     * @return this self process
     */
    public PolocloudProcess cloneClasspathJars() {
        FileCopyBuilder.builder().targetFolder("dependencies").prefix("polocloud-").files("api", "node", "daemon").suffix(".jar").copy();
        return this;
    }

    @SneakyThrows
    public void run() {
        var runtime = new Thread(() -> {
            try {
                var processBuilder = new ProcessBuilder();
                var process = processBuilder.start();

                process.waitFor();
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        runtime.setDaemon(false);
        runtime.start();
    }
}