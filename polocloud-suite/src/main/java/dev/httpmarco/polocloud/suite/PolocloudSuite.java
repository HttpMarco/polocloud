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
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;

@Getter
@Accessors(fluent = true)
public final class PolocloudSuite extends Polocloud {

    private static final Logger log = LogManager.getLogger(PolocloudSuite.class);
    private final I18n translation = new I18nPolocloudSuite();

    private boolean running = true;

    private final SuiteConfig config;
    private final CommandService commandService;
    private final PolocloudTerminal terminal;

    private final DependencyProvider dependencyProvider;
    private final ClusterProvider clusterProvider;
    private final ComponentProvider componentProvider;
    private final ClusterGroupProvider groupProvider;

    public PolocloudSuite() {
        config = SuiteConfig.load();

        // todo test
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        this.commandService = new CommandService();
        this.dependencyProvider = new DependencyProviderImpl();
        this.clusterProvider = new ClusterProviderImpl();
        this.componentProvider = new ComponentProviderImpl();
        this.groupProvider = new ClusterGroupProviderImpl();

        // start reading current terminal thread
        (terminal = new PolocloudTerminalImpl()).start();
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
        System.out.println("realod");
    }

    public void shutdown() {
        if(!this.running) {
            // cloud already shutdown
            return;
        }
        this.running = false;

        // unload only if component provider is present
        if (this.componentProvider != null) {
            this.componentProvider.close();
        }

        if(this.clusterProvider != null){
            this.clusterProvider.close();
        }

        AnsiConsole.systemUninstall();
        log.info("Shutting down Polocloud Suite...");
        System.exit(-1);
    }
}