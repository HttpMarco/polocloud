package dev.httpmarco.polocloud.base;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.dependencies.Dependency;
import dev.httpmarco.polocloud.base.logging.FileLoggerHandler;
import dev.httpmarco.polocloud.base.logging.LoggerOutPutStream;
import dev.httpmarco.polocloud.base.terminal.CloudTerminal;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class CloudBase extends CloudAPI {

    private boolean running = true;

    public CloudBase() {
        dependencyService().load(new Dependency("dev.httpmarco", "osgan-files", "1.1.15-SNAPSHOT", "1.1.15-20240514.180642-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        dependencyService().load(new Dependency("org.jline", "jline", "3.26.1"));
        dependencyService().load(new Dependency("org.fusesource.jansi", "jansi", "2.4.1"));

        // register logging layers (for general output)
        this.loggerFactory().registerLoggers(new FileLoggerHandler(), new CloudTerminal());

        System.setErr(new PrintStream(new LoggerOutPutStream(true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(), true, StandardCharsets.UTF_8));

        logger().info("Successfully started up!");

    }

    public void shutdown() {
        if (!running) {
            return;
        }
        running = false;

        logger().info("Shutdown cloud...");
        System.exit(0);
    }

    public static CloudBase instance() {
        return (CloudBase) CloudAPI.instance();
    }
}
