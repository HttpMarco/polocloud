package dev.httpmarco.polocloud.node.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class StringUtils {

    private static final String ALPHABET = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String randomString(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(RandomUtils.getRandomNumber(ALPHABET.length())));
        }

        return builder.toString();
    }
}
