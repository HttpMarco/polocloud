package dev.httpmarco.polocloud.node.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Path;

@Log4j2
@UtilityClass
public final class DownloadUtil {

    public void downloadFile(String url, String fileName, Path path) {
        var began = System.currentTimeMillis();
        var fullPath = (path == null) ? Path.of("").resolve(fileName) : path.resolve(fileName);

        try (var in = new URL(url).openStream();
             var out = new FileOutputStream(fullPath.toFile())) {

            var buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            log.info("Downloaded File from &b{}&7, took {}ms", url, (System.currentTimeMillis() - began));
        } catch (Exception e) {
            log.error("Error downloading file from {}: {}", url, e.getMessage());
        }
    }
}
