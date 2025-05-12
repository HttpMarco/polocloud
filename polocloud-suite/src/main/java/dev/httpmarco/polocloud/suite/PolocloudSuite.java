package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.event.EventProvider;
import dev.httpmarco.polocloud.suite.cluster.Cluster;
import dev.httpmarco.polocloud.suite.cluster.ClusterInitializer;
import dev.httpmarco.polocloud.suite.cluster.commands.ClusterCommand;
import dev.httpmarco.polocloud.suite.cluster.global.GlobalCluster;
import dev.httpmarco.polocloud.suite.commands.CommandService;
import dev.httpmarco.polocloud.suite.configuration.SuiteConfig;
import dev.httpmarco.polocloud.suite.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.suite.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.suite.events.EventProviderImpl;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupProviderImpl;
import dev.httpmarco.polocloud.suite.i18n.I18n;
import dev.httpmarco.polocloud.suite.i18n.impl.I18nPolocloudSuite;
import dev.httpmarco.polocloud.suite.platforms.PlatformProvider;
import dev.httpmarco.polocloud.suite.services.ClusterServiceProviderImpl;
import dev.httpmarco.polocloud.suite.templates.TemplateService;
import dev.httpmarco.polocloud.suite.terminal.PolocloudTerminal;
import dev.httpmarco.polocloud.suite.terminal.PolocloudTerminalImpl;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class PolocloudSuite extends Polocloud {

    private final SuiteConfig config;
    private final I18n translation;
    private final CommandService commandService;
    private final PolocloudTerminal terminal;

    private Cluster cluster;
    private final DependencyProvider dependencyProvider;
    private final EventProviderImpl eventProvider;
    private final PlatformProvider platformProvider;
    private final TemplateService templateService;
    private final ClusterGroupProviderImpl groupProvider;
    private final ClusterServiceProviderImpl serviceProvider;

    public PolocloudSuite() {
        this.config = SuiteConfig.load();
        this.translation = new I18nPolocloudSuite();
        this.eventProvider = new EventProviderImpl();
        this.commandService = new CommandService();
        this.dependencyProvider = new DependencyProviderImpl();
        this.cluster = ClusterInitializer.generate(config.cluster());
        this.platformProvider = new PlatformProvider();
        this.templateService = new TemplateService();
        this.groupProvider = new ClusterGroupProviderImpl();
        this.serviceProvider = new ClusterServiceProviderImpl();

        // start reading the current terminal thread
        (terminal = new PolocloudTerminalImpl()).start();

        // register cluster command -> we must wait for the cluster to be initialized
        PolocloudSuite.instance().commandService().registerCommand(new ClusterCommand());
    }

    public DependencyProvider dependencyProvider() {
        return this.dependencyProvider;
    }

    public static PolocloudSuite instance() {
        return (PolocloudSuite) Polocloud.instance();
    }

    public CommandService commandService() {
        return commandService;
    }

    public I18n translation() {
        return translation;
    }

    public void reload() {
        System.out.println("reload");
    }

    public void updateCluster(Cluster cluster) {
        this.cluster = cluster;

        // update the terminal prompt
        terminal.refresh();
    }

    public boolean externalAccess() {
        return cluster instanceof GlobalCluster;
    }
}