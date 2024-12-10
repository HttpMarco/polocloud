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
        // todo add more dependencies
        dependencyProvider.registerDependency(new DependencyImpl("com.google.code.gson", "gson", "2.11.0", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("org.jline", "jline", "3.27.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("org.apache.logging.log4j", "log4j-api", "2.24.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("org.apache.logging.log4j", "log4j-core", "2.24.1", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("io.netty", "netty5-common", "5.0.0.Alpha5", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("io.netty", "netty5-transport", "5.0.0.Alpha5", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("io.netty", "netty5-codec", "5.0.0.Alpha5", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("io.netty", "netty5-resolver", "5.0.0.Alpha5", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("io.netty", "netty5-transport-classes-epoll", "5.0.0.Alpha5", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("io.netty", "netty5-buffer", "5.0.0.Alpha5", "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s"));
        dependencyProvider.registerDependency(new DependencyImpl("dev.httpmarco", "netline", "1.0.2-SNAPSHOT", "https://s01.oss.sonatype.org/service/local/repositories/snapshots/content/%s/%s/%s/%s-%s", "20241209.200330-1"));

        new Node();
    }
}
