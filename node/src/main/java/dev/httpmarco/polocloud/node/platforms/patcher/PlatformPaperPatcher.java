package dev.httpmarco.polocloud.node.platforms.patcher;

import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public final class PlatformPaperPatcher implements PlatformPatcher{

    @Override
    @SneakyThrows
    public void patch(@NotNull File serverFile, ClusterLocalServiceImpl clusterLocalService) {
        var process = new ProcessBuilder("java", "-Dpaperclip.patchonly=true", "-jar", serverFile.getName()).directory(serverFile.getParentFile()).start();
        var inputStreamReader = new InputStreamReader(process.getInputStream());
        var bufferedReader = new BufferedReader(inputStreamReader);

        process.waitFor();
        process.destroyForcibly();
        bufferedReader.close();
        inputStreamReader.close();
    }

    @Contract(pure = true)
    @Override
    public @NotNull String id() {
        return "paperclip";
    }
}
