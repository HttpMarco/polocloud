package dev.httpmarco.polocloud.node.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
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
}
