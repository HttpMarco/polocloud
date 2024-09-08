package dev.httpmarco.polocloud.node.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class Downloader {

    @SneakyThrows
    public void download(String link, Path path) {
        Files.copy(urlStream(link), path);
    }

    @SneakyThrows
    private InputStream urlStream(String link) {
        return URI.create(link).toURL().openConnection().getInputStream();
    }
}
