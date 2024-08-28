package dev.httpmarco.polocloud.node.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

@UtilityClass
public final class StringUtils {

    private final String ALPHABET = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String randomString(int length) {
        var builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(RandomUtils.getRandomNumber(ALPHABET.length())));
        }

        return builder.toString();
    }

    @SneakyThrows
    public @NotNull String downloadStringContext(String link) {
        var stream = new URI(link).toURL().openStream();
        var context = new String(stream.readAllBytes());
        stream.close();
        return context;
    }
}
