package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.api.groups.ClusterGroupProvider;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.impl.ClusterProviderImpl;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;
import dev.httpmarco.polocloud.suite.components.impl.ComponentProviderImpl;
import dev.httpmarco.polocloud.suite.configuration.SuiteConfig;
import dev.httpmarco.polocloud.suite.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.suite.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.suite.groups.ClusterGroupProviderImpl;
import dev.httpmarco.polocloud.suite.i18n.I18n;
import dev.httpmarco.polocloud.suite.i18n.impl.I18nPolocloudSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;

public final class PolocloudSuite extends Polocloud {

    private static final Logger log = LogManager.getLogger(PolocloudSuite.class);

    private final I18n translation = new I18nPolocloudSuite();

    private final DependencyProvider dependencyProvider;
    private final ClusterProvider clusterProvider;
    private final ComponentProvider componentProvider;
    private final ClusterGroupProvider clusterGroupProvider;

    public PolocloudSuite() {
        var config = SuiteConfig.load();

        // todo test
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));

        this.dependencyProvider = new DependencyProviderImpl();
        this.clusterProvider = new ClusterProviderImpl(config.cluster());
        this.componentProvider = new ComponentProviderImpl();
        this.clusterGroupProvider = new ClusterGroupProviderImpl();
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

    public void close() {
        this.componentProvider.close();
    }

    public I18n translation() {
        return translation;
    }

}