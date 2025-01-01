package dev.httpmarco.polocloud.node.dependencies.utils;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.common.downloading.Downloader;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.impl.RepositoryDependency;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Pom {

    private static final Logger log = LogManager.getLogger(Pom.class);
    private final RepositoryDependency parentDependency;
    private final Document pom;
    private final List<Dependency> dependencies = new LinkedList<>();

    public Pom(RepositoryDependency parentDependency) {
        this.parentDependency = parentDependency;
        this.pom = Downloader.of(parentDependency.dependencyItemUrl() + "pom").xml();
        this.loadDependencies();
    }

    public void loadDependencies() {
        var dependencyNodes = pom.getElementsByTagName("dependency");

        for (int i = 0; i < dependencyNodes.getLength(); i++) {

            if (!(dependencyNodes.item(i) instanceof Element dependencyElement)) {
                continue;
            }

            var groupId = readTag(dependencyElement, "groupId");
            var artifactId = readTag(dependencyElement, "artifactId");
            var version = readTag(dependencyElement, "version");

            if (version == null || placeholder(version)) {
                //todo
                return;
            }

            this.dependencies.add(new RepositoryDependency(parentDependency.boundSlot(), groupId, artifactId, Version.parse(version)));
            log.debug("Add sub dependency from {} -> {}:{}", parentDependency.artifactId(), artifactId, version);
        }
    }

    public static @NotNull Pom reader(RepositoryDependency dependency) {
        return new Pom(dependency);
    }

    public List<Dependency> dependencies() {
        return dependencies;
    }

    private @Nullable String readTag(@NotNull Element element, String tag) {
        var nodeList = element.getElementsByTagName(tag);

        if (nodeList.getLength() == 0) {
            return null;
        }

        return nodeList.item(0).getTextContent();
    }

    public boolean placeholder(@NotNull String version) {
        return version.contains("${") && version.contains("}");
    }
}
