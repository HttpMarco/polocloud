package dev.httpmarco.polocloud.base;

import dev.httpmarco.osgan.files.json.JsonDocument;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.dependencies.Dependency;
import dev.httpmarco.polocloud.api.groups.CloudGroupProvider;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.api.services.CloudServiceProvider;
import dev.httpmarco.polocloud.base.common.PropertiesPoolSerializer;
import dev.httpmarco.polocloud.base.configuration.CloudConfiguration;
import dev.httpmarco.polocloud.base.groups.CloudServiceGroupProvider;
import dev.httpmarco.polocloud.base.logging.FileLoggerHandler;
import dev.httpmarco.polocloud.base.logging.LoggerOutPutStream;
import dev.httpmarco.polocloud.base.node.CloudNodeService;
import dev.httpmarco.polocloud.base.node.ExternalNode;
import dev.httpmarco.polocloud.base.node.LocalNode;
import dev.httpmarco.polocloud.base.services.CloudServiceProviderImpl;
import dev.httpmarco.polocloud.base.templates.TemplatesService;
import dev.httpmarco.polocloud.base.terminal.CloudTerminal;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudBase extends CloudAPI {

    private final CloudTerminal terminal;
    private final NodeService nodeService;
    private final CloudGroupProvider groupProvider;
    private final CloudServiceProvider serviceProvider;
    private final TemplatesService templatesService;

    private boolean running = true;

    public CloudBase() {
        dependencyService().load(new Dependency("com.google.code.gson", "gson", "2.10.1"));
        dependencyService().load(new Dependency("org.jline", "jline", "3.26.1"));
        dependencyService().load(new Dependency("org.fusesource.jansi", "jansi", "2.4.1"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown(true)));

        var cloudConfiguration = loadConfiguration();

        this.terminal = new CloudTerminal();
        // register logging layers (for general output)
        this.loggerFactory().registerLoggers(new FileLoggerHandler(), terminal);

        System.setErr(new PrintStream(new LoggerOutPutStream(true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(), true, StandardCharsets.UTF_8));

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());

        this.nodeService = new CloudNodeService(new LocalNode(cloudConfiguration.clusterId(), cloudConfiguration.clusterName(), "127.0.0.1", 8879), cloudConfiguration.externalNodes());

        // print cloud header information
        terminal.spacer();
        terminal.spacer("   &3PoloCloud &2- &1Simple minecraft cloudsystem &2- &1v1.0.12");
        terminal.spacer("   &1node&2: &1" + nodeService.localNode().name() + " &2| &1id&2: &1" + nodeService.localNode().id());
        terminal.spacer();

        this.nodeService.localNode().initialize();
        this.groupProvider = new CloudServiceGroupProvider();
        this.templatesService = new TemplatesService();
        this.serviceProvider = new CloudServiceProviderImpl();

        logger().info("Successfully started up&2!");

        this.terminal.start();
    }

    public void shutdown(boolean shutdownCycle) {
        if (!running) {
            return;
        }
        running = false;

        logger().info("Shutdown cloud...");
        ((CloudServiceProviderImpl) serviceProvider).close();

        for (var service : this.serviceProvider.services()) {
            service.shutdown();
        }

        this.nodeService.localNode().close();

        logger().info("Cloud successfully stopped!");
        this.loggerFactory().close();

        if (!shutdownCycle) {
            System.exit(0);
        }
    }

    public CloudConfiguration loadConfiguration() {
        return new JsonDocument<>(new CloudConfiguration(UUID.randomUUID(), "node-1", 10000, new ExternalNode[0]), Path.of("config.json"), new PropertiesPoolSerializer()).value();
    }

    public static CloudBase instance() {
        return (CloudBase) CloudAPI.instance();
    }
}
