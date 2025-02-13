package dev.httpmarco.polocloud.suite;

import dev.httpmarco.polocloud.api.Polocloud;
import dev.httpmarco.polocloud.suite.cluster.ClusterProvider;
import dev.httpmarco.polocloud.suite.cluster.impl.ClusterProviderImpl;
import dev.httpmarco.polocloud.suite.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.suite.dependencies.impl.DependencyProviderImpl;
import dev.httpmarco.polocloud.suite.i18n.I18n;
import dev.httpmarco.polocloud.suite.i18n.impl.I18nPolocloudSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PolocloudSuite extends Polocloud {

    private static final Logger log = LogManager.getLogger(PolocloudSuite.class);
    private static final I18n translation = new I18nPolocloudSuite();

    private final DependencyProvider dependencyProvider;
    private final ClusterProvider clusterProvider;

    public PolocloudSuite() {
        this.dependencyProvider = new DependencyProviderImpl();
        this.clusterProvider = new ClusterProviderImpl();
    }

    public DependencyProvider dependencyProvider() {
        return this.dependencyProvider;
    }

    public static PolocloudSuite instance() {
        return instance();
    }
}