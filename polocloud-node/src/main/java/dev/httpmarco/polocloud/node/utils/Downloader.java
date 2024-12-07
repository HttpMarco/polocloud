package dev.httpmarco.polocloud.node.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;

@UtilityClass
public final class Downloader {

    @SneakyThrows
    public String downloadString(String urlPath) {
        var url = new URL(urlPath);

        try (var stream = url.openStream()) {
            return new String(stream.readAllBytes());
        }
    }
}
