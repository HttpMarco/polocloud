package dev.httpmarco.polocloud.node;

import dev.httpmarco.polocloud.node.dependencies.impl.DependencyFactoryImpl;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyImpl;
import org.junit.jupiter.api.Test;

public class SubDependencyTest {

    @Test
    public void loadAllSubDependency() {
        var factory =  new DependencyFactoryImpl();

        assert !factory.loadSubDependencies(new DependencyImpl("org.jline", "jline", "3.27.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s")).isEmpty();
    }
}
