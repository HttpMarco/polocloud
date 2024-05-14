package dev.httpmarco.polocloud.base;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.dependencies.Dependency;
import dev.httpmarco.polocloud.base.logging.FileLoggerHandler;
import dev.httpmarco.polocloud.base.logging.LoggerOutPutStream;
import dev.httpmarco.polocloud.base.terminal.CloudTerminal;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudBase extends CloudAPI {

    private final CloudTerminal terminal;

    private boolean running = true;

    public CloudBase() {
        dependencyService().load(new Dependency("dev.httpmarco", "osgan-files", "1.1.15-SNAPSHOT", "1.1.15-20240514.180642-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        dependencyService().load(new Dependency("org.jline", "jline", "3.26.1"));
        dependencyService().load(new Dependency("org.fusesource.jansi", "jansi", "2.4.1"));

        this.terminal = new CloudTerminal();
        // register logging layers (for general output)
        this.loggerFactory().registerLoggers(new FileLoggerHandler(), terminal);

        System.setErr(new PrintStream(new LoggerOutPutStream(true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(), true, StandardCharsets.UTF_8));

        // print cloud header informations
        terminal.spacer();
        terminal.spacer("   &3PoloCloud &2- &1Simple minecraft cloudsystem &2- &1v1.0.12");
        terminal.spacer("   &1node&2: &1node-1 &2| &1id&2: &1" + UUID.randomUUID());
        terminal.spacer();

        logger().info("Successfully started up!");
        this.terminal.start();
    }

    public void shutdown() {
        if (!running) {
            return;
        }
        running = false;

        logger().info("Shutdown cloud...");

        this.terminal.close();
        System.exit(0);
    }

    public static CloudBase instance() {
        return (CloudBase) CloudAPI.instance();
    }
}
