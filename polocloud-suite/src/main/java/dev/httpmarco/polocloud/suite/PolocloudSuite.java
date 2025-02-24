package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.impl.ClusterProviderImpl;
import dev.httpmarco.polocloud.suite.components.ComponentProvider;
import dev.httpmarco.polocloud.suite.components.impl.ComponentProviderImpl;
import dev.httpmarco.polocloud.suite.configuration.SuiteConfig;
import dev.httpmarco.polocloud.suite.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.suite.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.suite.i18n.I18n;
import dev.httpmarco.polocloud.suite.i18n.impl.I18nPolocloudSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PolocloudSuite extends Polocloud {

    private static final Logger log = LogManager.getLogger(PolocloudSuite.class);

    private final I18n translation = new I18nPolocloudSuite();

    private final DependencyProvider dependencyProvider;
    private final ClusterProvider clusterProvider;
    private final ComponentProvider componentProvider;

    public PolocloudSuite() {

        var config = SuiteConfig.load();

        // todo test
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));

        this.dependencyProvider = new DependencyProviderImpl();
        this.clusterProvider = new ClusterProviderImpl(config.localSuiteData());
        this.componentProvider = new ComponentProviderImpl();
    }

    public DependencyProvider dependencyProvider() {
        return this.dependencyProvider;
    }

    public static PolocloudSuite instance() {
        return instance();
    }

    public void close() {
        this.componentProvider.close();
    }

    public I18n translation() {
        return translation;
    }

}