package dev.httpmarco.polocloud.suite.utils.downloading;

import dev.httpmarco.polocloud.suite.utils.GsonInstance;
import dev.httpmarco.polocloud.suite.utils.PathUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class Downloader {

    private static final DocumentBuilderFactory XML_FACTORY = DocumentBuilderFactory.newInstance();
    private final String url;

    public Downloader(String url) {
        this.url = url;
    }

    public static Downloader of(String url) {
        return new Downloader(url);
    }

    public File file(String pathUrl) {
        var path = PathUtils.defineDirectory(pathUrl);
        content(it -> Files.copy(it, path, StandardCopyOption.REPLACE_EXISTING));
        return path.toFile();
    }

    public Document xml() throws ParserConfigurationException {
        return content(it -> XML_FACTORY.newDocumentBuilder().parse(it));
    }

    public <T> T gson(Class<T> tClass) {
        return GsonInstance.DEFAULT.fromJson(plain(), tClass);
    }

    public String plain() {
        return content(it -> new String(it.readAllBytes(), StandardCharsets.UTF_8));
    }

    private <T> T content(DownloadAcceptor<T> acceptor) {
        try (var in = new URL(url).openStream()) {
            return acceptor.accept(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download from " + url, e);
        }
    }

}
