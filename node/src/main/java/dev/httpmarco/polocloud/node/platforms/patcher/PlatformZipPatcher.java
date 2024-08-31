package dev.httpmarco.polocloud.node.platforms.patcher;

import dev.httpmarco.polocloud.node.services.ClusterLocalServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public final class PlatformZipPatcher implements PlatformPatcher {

    @Override
    @SneakyThrows
    public void patch(File serverFile, ClusterLocalServiceImpl clusterLocalService) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(serverFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                if (fileName.endsWith(".jar")) {
                    File newFile = new File(serverFile.getParent() + "/" + fileName);
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    zis.close();
                    serverFile.delete();
                    Files.copy(Path.of(serverFile.getParent() +  "/" + fileName), serverFile.toPath());
                    newFile.delete();
                    break;
                }
            }
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull String id() {
        return "zip";
    }
}
