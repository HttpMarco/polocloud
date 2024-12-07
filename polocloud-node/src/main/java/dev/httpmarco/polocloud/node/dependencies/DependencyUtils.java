package dev.httpmarco.polocloud.node.dependencies;

import dev.httpmarco.polocloud.node.utils.Downloader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class DependencyUtils {

    @SneakyThrows
    public String readChecksum(@NotNull Dependency dependency) {
        return Downloader.downloadString(dependency.url() + ".jar.sha1");
    }

    public boolean isStableVersion(@NotNull String version) {
        return !(version.contains("alpha") || version.contains("beta") || version.contains("snapshot"));
    }
}
