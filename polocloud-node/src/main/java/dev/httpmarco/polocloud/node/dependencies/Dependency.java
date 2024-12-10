package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.api.Available;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a dependency/library
 */
public interface Dependency extends Available {

    /**
     * The group id of the dependency
     * @return the group id
     */
    String groupId();

    /**
     * The artifact id of the dependency
     * @return the artifact id
     */
    String artifactId();

    /**
     * The version of the dependency
     * @return the version
     */
    String version();

    /**
     * The checksum of the dependency
     * Default is "sha1"
     * @return the checksum
     */
    String checksum();

    /**
     * The classifier of the dependency
     * @return the classifier
     */
    @Nullable
    String classifier();

    /**
     * The repository where the dependency is located
     * Default is "<a href="https://repo1.maven.org/maven2/">...</a>"
     * @return the repository url
     */
    String repository();

    /**
     * The dependencies of the dependency
     * @return the dependencies
     */
    Collection<Dependency> depend();

    /**
     * The url of the pom file
     * @return the pom url
     */
    String url();

    /**
     * The name of the file
     * @return the file name
     */
    String fileName();

}
