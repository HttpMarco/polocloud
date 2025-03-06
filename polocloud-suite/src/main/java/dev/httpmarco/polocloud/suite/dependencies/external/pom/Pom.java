package dev.httpmarco.polocloud.suite.dependencies.external.pom;

import dev.httpmarco.polocloud.suite.dependencies.exception.DependencyChecksumNotMatchException;
import dev.httpmarco.polocloud.suite.dependencies.external.ExternalDependency;
import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class Pom {

    public static final String URL_PATTERN = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s";

    private final ExternalDependency parentDependency;
    private final String parentDependencyUrl;

    // all declared dependencies
    private final List<ExternalDependency> dependencies = new ArrayList<>();

    public Pom(ExternalDependency dependency) {
        this.parentDependency = dependency;
        this.parentDependencyUrl = URL_PATTERN.formatted(dependency.groupIdPath(), dependency.artifactId(), dependency.version().versionWithState(), dependency.artifactId(), dependency.version().versionWithState());

        // load pom, dependencies and versions
        this.loadContext(0);
    }

    private void loadContext(int retries) {
        // maximum retries are 3
        if (retries > 3) {
            throw new DependencyChecksumNotMatchException(parentDependency);
        }
        try {
            var pom = Downloader.of(this.parentDependencyUrl + ".pom").xml();
            var projectNodes = pom.getElementsByTagName("project");

            if (projectNodes.getLength() == 0) {
                return;
            }

            var projectElement = (Element) projectNodes.item(0);

            var children = projectElement.getChildNodes();
            Element dependenciesElement = null;
            for (var i = 0; i < children.getLength(); i++) {
                if (children.item(i) instanceof Element element && element.getTagName().equals("dependencies")) {
                    dependenciesElement = element;
                    break;
                }
            }

            if (dependenciesElement == null) {
                return;
            }

            var dependencyNodes = dependenciesElement.getChildNodes();

            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                if (!(dependencyNodes.item(i) instanceof Element dependencyElement)) {
                    continue;
                }

                if (!dependencyElement.getTagName().equals("dependency")) {
                    continue;
                }

                var groupId = readTag(dependencyElement, "groupId");
                var artifactId = readTag(dependencyElement, "artifactId");
                var version = readTag(dependencyElement, "version");

                if (version == null || placeholder(version)) {
                    continue;
                }

                var scope = readTag(dependencyElement, "scope");
                if (scope == null || scope.equals("test") || scope.equals("runtime")) {
                    continue;
                }

                // we not need optional dependencies
                var optional = readTag(dependencyElement, "optional");
                if (optional != null && optional.equals("true")) {
                    continue;
                }

                var subDependency = new ExternalDependency(groupId, artifactId, version);
                dependencies.add(subDependency);
            }
        } catch (ParserConfigurationException e) {
            this.loadContext(++retries);
        }
    }


    private String readTag(Element element, String tag) {
        var nodeList = element.getElementsByTagName(tag);

        if (nodeList.getLength() == 0) {
            return null;
        }

        return nodeList.item(0).getTextContent();
    }

    public boolean placeholder(String version) {
        return version.contains("${") && version.contains("}");
    }

    public List<ExternalDependency> dependencies() {
        return Collections.unmodifiableList(dependencies);
    }
}