package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.common.downloading.Downloader;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.slots.ClassloaderDependencySlot;
import dev.httpmarco.polocloud.node.dependencies.utils.Pom;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Log4j2
@ToString
@Accessors(fluent = true)
public final class RepositoryDependency implements Dependency {

    // this dependency is bound to this slot
    private final ClassloaderDependencySlot boundSlot;

    private final String groupId;
    private final String artifactId;
    private final Version version;

    private final String dependencyItemUrl;
    private final @Nullable String classifier;
    private @Nullable File file;

    public RepositoryDependency(ClassloaderDependencySlot boundSlot, @NotNull String groupId, String artifactId, Version version, @Nullable String classifier, @NotNull String repositoryUrl) {
        this.boundSlot = boundSlot;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
        this.dependencyItemUrl = repositoryUrl.formatted(groupId.replace(".", "/"), artifactId, version, artifactId, (classifier == null ? version : version.semantic() + "-" + classifier) + ".");
    }

    public RepositoryDependency(ClassloaderDependencySlot boundSlot, @NotNull String groupId, String artifactId, Version version) {
        this(boundSlot, groupId, artifactId, version, null, "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s");
    }

    @Override
    public boolean available() {
        return file != null && file.exists();
    }

    @Override
    public void prepare() {
        // we must check all sub dependencies
        var pom = Pom.reader(this);

        // if there are no sub dependencies, we can skip this step
        for (var dependency : pom.dependencies()) {
            this.boundSlot.bindDependencies(dependency);
        }
    }

    @Override
    public void initialize() {
        var downloadTarget = Path.of("dependencies").resolve(artifactId + "-" + version + ".jar");
        log.debug("Preparing dependency {}:{}", artifactId, version);

        if (Files.exists(downloadTarget)) {
            log.debug("Dependency {}:{} already exists. Skip!", artifactId, version);
            this.file = downloadTarget.toFile();
            return;
        }

        log.info("Downloading dependency {}:{}...", artifactId, version);
        this.file = Downloader.of(dependencyItemUrl + "jar").file(downloadTarget);
    }
}
