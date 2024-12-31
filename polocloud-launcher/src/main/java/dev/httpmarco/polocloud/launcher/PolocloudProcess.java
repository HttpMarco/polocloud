package dev.httpmarco.polocloud.launcher;

import lombok.SneakyThrows;

public final class PolocloudProcess extends Thread {

    public PolocloudProcess() {
        setDaemon(false);
    }

    @Override
    @SneakyThrows
    public void run() {
        var processBuilder = new ProcessBuilder();
        processBuilder.inheritIO().command("java", "-jar", "dependencies/polocloud-node-" + System.getProperty("version") + ".jar");

        var process = processBuilder.start();
        process.waitFor();
    }
}