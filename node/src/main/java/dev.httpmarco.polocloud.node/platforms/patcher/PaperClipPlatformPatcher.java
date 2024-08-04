package dev.httpmarco.polocloud.node.platforms.patcher;

import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.node.platforms.Platform;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Log4j2
public final class PaperClipPlatformPatcher extends AbstractPlatformPatcher {

    public PaperClipPlatformPatcher() {
        super("paperclip");
    }

    @Override
    public @NotNull CompletableFuture<Void> patch(ClusterGroup clusterGroup, File file) {
        var future = new CompletableFuture<Void>();

        new Thread(() -> {
            log.info("Start patching of &f{}&8...", clusterGroup.platform().details());
            try {
                final var process = new ProcessBuilder("java", "-Dpaperclip.patchonly=true", "-jar", file.getName()).directory(file.getParentFile()).start();
                process.waitFor();
                process.destroyForcibly();
                log.info("Successfully patch &f{}&8!", clusterGroup.platform().details());
                future.complete(null);
            } catch (InterruptedException | IOException e) {
                future.completeExceptionally(e);
                e.printStackTrace();
            }
        }).start();

        return future;
    }
}