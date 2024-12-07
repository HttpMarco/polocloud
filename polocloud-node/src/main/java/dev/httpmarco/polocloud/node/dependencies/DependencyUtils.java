package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.node.utils.Downloader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DependencyUtils {

    @SneakyThrows
    public String readChecksum(Dependency dependency) {
        return Downloader.downloadString(dependency.url() + ".jar.sha256");
    }
}
