package dev.httpmarco.polocloud.node.boot;

import dev.httpmarco.polocloud.node.Node;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyImpl;
import dev.httpmarco.polocloud.node.dependencies.impl.DependencyProviderImpl;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.instrument.Instrumentation;

@Accessors(fluent = true)
public final class NodeBoot {

    @Getter
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrument) {
        instrumentation = instrument;
    }

    public static void main(String[] args) {
        var dependencyProvider = new DependencyProviderImpl();

        // download required dependencies
        dependencyProvider.registerDependency(new DependencyImpl("org.jline", "jline", "3.27.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));

        dependencyProvider.registerDependency(new DependencyImpl("org.apache.logging.log4j", "log4j-api", "2.24.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("org.apache.logging.log4j", "log4j-core", "2.24.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));

        new Node();
    }
}
