package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencyFactory;
import dev.httpmarco.polocloud.node.dependencies.DependencyUtils;
import dev.httpmarco.polocloud.node.dependencies.exceptions.UnkownDependencyVersionException;
import dev.httpmarco.polocloud.node.dependencies.validator.ChecksumValidator;
import dev.httpmarco.polocloud.node.dependencies.xml.DependencyScheme;
import dev.httpmarco.polocloud.node.utils.Downloader;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;

public final class DependencyFactoryImpl implements DependencyFactory {

    private static final ChecksumValidator VALIDATOR = new ChecksumValidator();

    @Override
    public void prepare(Dependency dependency) {



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

            dependencies.add(subDependency);
            dependencies.addAll(loadSubDependencies(subDependency));
        }
        return dependencies;
    }

    @Contract(pure = true)
    @SneakyThrows
    private String detectLatestVersion(DependencyScheme scheme) {
        var metadata = Downloader.downloadXmlDocument(String.format("https://repo1.maven.org/maven2/%s/%s/maven-metadata.xml", scheme.groupId().replace(".", "/"), scheme.artifactId()));
        var versionsElement = (Element) metadata.getElementsByTagName("versions").item(0);
        var versionNodes = versionsElement.getElementsByTagName("version");

        String latestStableVersion = null;
        for (int i = 0; i < versionNodes.getLength(); i++) {
            var version = versionNodes.item(i).getTextContent();

            if (!DependencyUtils.isStableVersion(version)) {
                continue;
            }

            // we only allow stable versions
            latestStableVersion = version;
        }

        if (latestStableVersion != null) {
            return latestStableVersion;
        }
        throw new UnkownDependencyVersionException(scheme);
    }
}