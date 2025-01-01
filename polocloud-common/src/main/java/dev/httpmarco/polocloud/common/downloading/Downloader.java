package dev.httpmarco.polocloud.common.downloading;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@NoArgsConstructor(access = AccessLevel.NONE)
@AllArgsConstructor
public class Downloader {

    private static final DocumentBuilderFactory XML_FACTORY = DocumentBuilderFactory.newInstance();
    private final String url;

    @Contract("_ -> new")
    public static @NotNull Downloader of(String url) {
        return new Downloader(url);
    }

    @SneakyThrows
    public File file(@NotNull Path path) {
        Files.createDirectories(path.getParent());
        content(it -> Files.copy(it, path, StandardCopyOption.REPLACE_EXISTING));
        return path.toFile();
    }

    @SneakyThrows
    public Document xml() {
        var documentBuilder = XML_FACTORY.newDocumentBuilder();
        return content(documentBuilder::parse);
    }


    private <T> T content(@NotNull DownloadAcceptor<T> acceptor) {
        try (var in = new URL(url).openStream()) {
            return acceptor.accept(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download from " + url, e);
        }
    }
}