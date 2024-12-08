package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.boot.NodeBoot;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyFactory;
import dev.httpmarco.polocloud.node.dependencies.DependencyProvider;
import dev.httpmarco.polocloud.node.dependencies.validator.ChecksumValidator;
import dev.httpmarco.polocloud.node.dependencies.xml.DependencyScheme;
import dev.httpmarco.polocloud.node.utils.Downloader;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public final class DependencyFactoryImpl implements DependencyFactory {

    private static final ChecksumValidator VALIDATOR = new ChecksumValidator();

    private final DependencyProvider provider;
    private final Path dependencyPath = Path.of("dependencies");

    @SneakyThrows
    public DependencyFactoryImpl(DependencyProvider provider) {
        this.provider = provider;

        if(Files.exists(dependencyPath)) {
            return;
        }

        Files.createDirectory(dependencyPath);
    }

    @SneakyThrows
    @Override
    public void prepare(@NotNull Dependency dependency) {
        if(!dependency.available()) {
            return;
        }

        if(provider.loadedDependencies().contains(dependency)) {
            return;
        }

        for (var subDependency : dependency.depend()) {
            prepare(subDependency);
        }

        var dependencyPath = this.dependencyPath.resolve(dependency.fileName());

        if(Files.exists(dependencyPath) && VALIDATOR.valid(dependency, dependencyPath)) {
            NodeBoot.instrumentation().appendToSystemClassLoaderSearch(new JarFile(dependencyPath.toFile()));
            return;
        }

        Downloader.download(dependency.url() + ".jar", dependencyPath);
        NodeBoot.instrumentation().appendToSystemClassLoaderSearch(new JarFile(dependencyPath.toFile()));
        System.out.println("Loaded dependency " + dependency.fileName());
    }

    @Override
    public void uninstall(Dependency dependency) {

    }

    @Contract(pure = true)
    @Override
    public @NotNull List<Dependency> loadSubDependencies(@NotNull Dependency dependency) {
        var dependencies = new ArrayList<Dependency>();

        var pom = Downloader.downloadXmlDocument(dependency.url() + ".pom");
        var pomDependencies = pom.getElementsByTagName("dependency");

        for (int i = 0; i < pomDependencies.getLength(); i++) {
            var xmlDependency = new DependencyScheme(pomDependencies.item(i));

            // testing or optional dependencies we don't need
            if(xmlDependency.scope() != null && xmlDependency.scope().equals("test") || xmlDependency.optional()) {
                continue;
            }

            if(xmlDependency.version() == null) {
                xmlDependency.version(detectLatestVersion(xmlDependency));
            }

            var subDependency = new DependencyImpl(xmlDependency, dependency.repository());
            // we must load all sub dependencies recursively
            subDependency.depend().addAll(loadSubDependencies(subDependency));
            dependencies.add(subDependency);
        }
        return dependencies;
    }

    /**
     * Detects the latest version of a dependency
     * @param scheme the xml dependency scheme
     * @return the latest version
     */
    @Contract(pure = true)
    @SneakyThrows
    private String detectLatestVersion(@NotNull DependencyScheme scheme) {
        return Downloader.downloadXmlDocument(String.format("https://repo1.maven.org/maven2/%s/%s/maven-metadata.xml", scheme.urlGroupId(), scheme.artifactId()))
                .getElementsByTagName("release")
                .item(0)
                .getTextContent();
    }
}