package dev.httpmarco.polocloud.node.dependencies.impl;

import dev.httpmarco.polocloud.api.Version;
import dev.httpmarco.polocloud.common.downloading.Downloader;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import dev.httpmarco.polocloud.node.dependencies.DependencySlot;
import dev.httpmarco.polocloud.node.dependencies.utils.Pom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Getter
@Log4j2
@ToString
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class RepositoryDependency implements Dependency {

    // this dependency is bound to this slot
    private final DependencySlot boundSlot;

    private final String groupId;
    private final String artifactId;
    private final Version version;

    private final String repository = "https://repo1.maven.org/maven2/";
    private @Nullable File file;

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
        var downloadUrl = this.repository + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/" + downloadTarget.getFileName().getFileName();

        this.file = Downloader.of(downloadUrl).file(downloadTarget);
    }
}
