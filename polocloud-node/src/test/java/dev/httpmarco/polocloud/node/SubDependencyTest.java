package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyImpl;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyProviderImpl;
import org.junit.jupiter.api.Test;

public class SubDependencyTest {

    private static final DependencyProvider PROVIDER = new DependencyProviderImpl();
    private static final Dependency TEST_DEPENDENCY = new DependencyImpl("com.google.code.gson", "gson", "2.11.0", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s");

    @Test
    public void loadAllSubDependency() {
        assert !PROVIDER.factory().loadSubDependencies(TEST_DEPENDENCY).isEmpty();
    }

    @Test
    public void registerDependency() {
        PROVIDER.registerDependency(TEST_DEPENDENCY);
    }
}
