package dev.httpmarco.polocloud.modules.rest.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class EncryptionUtil {

    private static final Argon2 ARGON_2 = Argon2Factory.create();

    public static String encrypt(String content) {
        var contentCharArray = content.toCharArray();
        try {
            return ARGON_2.hash(2, 65536, 1, contentCharArray);
        } finally {
            ARGON_2.wipeArray(contentCharArray);
        }
    }

    public static boolean verify(String hash, String content) {
        var contentCharArray = content.toCharArray();
        try {
            return ARGON_2.verify(hash, contentCharArray);
        } finally {
            ARGON_2.wipeArray(contentCharArray);
        }
    }

}