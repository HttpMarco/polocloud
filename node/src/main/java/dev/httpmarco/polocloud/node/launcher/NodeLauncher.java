package dev.httpmarco.polocloud.node.launcher;

import dev.httpmarco.polocloud.launcher.dependency.Dependency;
import dev.httpmarco.polocloud.launcher.dependency.DependencyDownloader;
import dev.httpmarco.polocloud.node.Node;
import lombok.SneakyThrows;

public class NodeLauncher {

    @SneakyThrows
    public static void main(String[] args) {

        var log4jCoreDependencyApi = new Dependency("org.apache.logging.log4j", "log4j-api", "2.23.1");
        var log4jCoreDependency = new Dependency("org.apache.logging.log4j", "log4j-core", "2.23.1");
        var log4jSlf4jDependency = new Dependency("org.apache.logging.log4j", "log4j-slf4j2-impl", "2.23.1");
        var jlineDependency = new Dependency("org.jline", "jline", "3.26.3");

        // load logging dependencies
        DependencyDownloader.download( new Dependency("org.slf4j", "slf4j-api", "2.0.13"), log4jCoreDependencyApi, log4jCoreDependency, log4jSlf4jDependency, jlineDependency);

        try {
            System.setProperty("startup", String.valueOf(System.currentTimeMillis()));
            new Node();
        } catch (Exception exception) {
            // check for remove
            exception.printStackTrace();
        }
    }
}
