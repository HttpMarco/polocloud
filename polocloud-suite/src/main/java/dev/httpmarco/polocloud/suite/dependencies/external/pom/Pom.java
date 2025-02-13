package dev.httpmarco.polocloud.suite.dependencies.external.pom;

import dev.httpmarco.polocloud.suite.dependencies.exception.DependencyChecksumNotMatchException;
import dev.httpmarco.polocloud.suite.dependencies.external.ExternalDependency;
import dev.httpmarco.polocloud.suite.utils.downloading.Downloader;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Pom {

    public static final String URL_PATTERN = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s";

    private final ExternalDependency parentDependency;
    private final String parentDependencyUrl;

    // all declared dependencies
    private final List<ExternalDependency> dependencies = new ArrayList<>();

    public Pom(ExternalDependency dependency) {
        this.parentDependency = dependency;
        this.parentDependencyUrl = URL_PATTERN.formatted(dependency.groupIdPath(), dependency.artifactId(), dependency.version(), dependency.artifactId(), dependency.version());

        // load pom, dependencies and versions
        this.loadContext(0);
    }

    private void loadContext(int retries) {
        // maximum retries are 3
        if (retries > 3) {
            // at the moment the checksum is not correct
            throw new DependencyChecksumNotMatchException(parentDependency);
        }
        try {
            var pom = Downloader.of(this.parentDependencyUrl + ".pom").xml();
            var dependencyNodes = pom.getElementsByTagName("dependency");

            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                if (!(dependencyNodes.item(i) instanceof Element dependencyElement)) {
                    continue;
                }

                var groupId = readTag(dependencyElement, "groupId");
                var artifactId = readTag(dependencyElement, "artifactId");
                var version = readTag(dependencyElement, "version");

                // we need to search the current dependency version
                if (version == null || placeholder(version)) {
                    //todo
                    return;
                }

                var scope = readTag(dependencyElement, "scope");

                if(scope != null && scope.equals("test")) {
                    // we not need this
                    continue;
                }

                // we load the complete context of this sub dependency
                var subDependency = new ExternalDependency(groupId, artifactId, version);
                dependencies.add(subDependency);
            }
        } catch (ParserConfigurationException e) {
            // another try for the downloading
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