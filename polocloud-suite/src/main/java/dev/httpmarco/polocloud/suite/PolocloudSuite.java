package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.impl.ClusterProviderImpl;
import dev.httpmarco.polocloud.suite.commands.CommandService;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;
import dev.httpmarco.polocloud.suite.components.impl.ComponentProviderImpl;
import dev.httpmarco.polocloud.suite.configuration.SuiteConfig;
import dev.httpmarco.polocloud.suite.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.suite.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupProviderImpl;
import dev.httpmarco.polocloud.suite.i18n.I18n;
import dev.httpmarco.polocloud.suite.i18n.impl.I18nPolocloudSuite;
import dev.httpmarco.polocloud.suite.terminal.PolocloudTerminal;
import dev.httpmarco.polocloud.suite.terminal.PolocloudTerminalImpl;
import org.fusesource.jansi.AnsiConsole;

public final class PolocloudSuite extends Polocloud {

    private final I18n translation = new I18nPolocloudSuite();

    private boolean running = true;

    private final CommandService commandService;
    private final PolocloudTerminal terminal;

    private final DependencyProvider dependencyProvider;
    private final ClusterProvider clusterProvider;
    private final ComponentProvider componentProvider;
    private final ClusterGroupProvider clusterGroupProvider;

    public PolocloudSuite() {
        var config = SuiteConfig.load();

        // todo test
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        this.dependencyProvider = new DependencyProviderImpl();
        this.clusterProvider = new ClusterProviderImpl(config.cluster());
        this.componentProvider = new ComponentProviderImpl();
        this.clusterGroupProvider = new ClusterGroupProviderImpl();

        this.commandService = new CommandService();

        // start reading current terminal thread
        (terminal = new PolocloudTerminalImpl()).start();
    }

    public DependencyProvider dependencyProvider() {
        return this.dependencyProvider;
    }

    public static PolocloudSuite instance() {
        return (PolocloudSuite) Polocloud.instance();
    }

    @Override
    public ClusterGroupProvider groupProvider() {
        return clusterGroupProvider;
    }

    public CommandService commandService() {
        return commandService;
    }

    public I18n translation() {
        return translation;
    }

    public void reload() {
        System.out.println("realod");
    }

    public void shutdown() {
        if(!this.running) {
            // cloud already shutdown
            return;
        }
        this.running = false;

        // unload only if component provider is present
        if (componentProvider != null) {
            this.componentProvider.close();
        }

        AnsiConsole.systemUninstall();
        System.out.println("Shutting down Polocloud Suite...");
        System.exit(-1);
    }
}