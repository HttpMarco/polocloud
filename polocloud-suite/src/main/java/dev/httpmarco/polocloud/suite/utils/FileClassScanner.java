package dev.httpmarco.polocloud.suite.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class FileClassScanner {

    public static Set<String> classes(File jarFile) throws IOException {
        Set<String> classes = new HashSet<>();
        try (var jar = new JarFile(jarFile)) {
            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace('/', '.')
                            .replace(".class", "");
                    classes.add(className);
                }
            }
        }
        return classes;
    }
}
