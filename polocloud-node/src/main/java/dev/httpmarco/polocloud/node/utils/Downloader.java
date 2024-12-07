package dev.httpmarco.polocloud.node.utils;

import dev.httpmarco.polocloud.node.dependencies.Dependency;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

@UtilityClass
public final class Downloader {

    @SneakyThrows
    public String downloadString(String urlPath) {
        var url = new URL(urlPath);

        try (var stream = url.openStream()) {
            return new String(stream.readAllBytes());
        }
    }

    @SneakyThrows
    public Document downloadXmlDocument(String urlPath) {
        var url = new URL(urlPath);
        var connection = (HttpURLConnection) url.openConnection();
        try (var inputStream = connection.getInputStream()) {
            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(inputStream));
        }
    }

    @SneakyThrows
    public void download(String url, Path path) {
        try (var inputStream = new URL(url).openStream();
             var outputStream = new BufferedOutputStream(new FileOutputStream(path.toString()))) {

            var buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
