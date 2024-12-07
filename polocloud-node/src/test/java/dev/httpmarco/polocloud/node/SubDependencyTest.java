package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyFactory;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyFactoryImpl;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyImpl;
import org.junit.jupiter.api.Test;

public class SubDependencyTest {

    private static final DependencyFactory FACTORY =  new DependencyFactoryImpl();
    private static final Dependency TEST_DEPENDENCY = new DependencyImpl("com.google.code.gson", "gson", "2.11.0", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s");

    @Test
    public void loadAllSubDependency() {
        assert !FACTORY.loadSubDependencies(TEST_DEPENDENCY).isEmpty();
    }
}
