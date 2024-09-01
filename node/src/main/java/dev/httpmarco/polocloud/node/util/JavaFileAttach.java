package dev.httpmarco.polocloud.node.util;

import dev.httpmarco.polocloud.node.Node;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

@Slf4j
@UtilityClass
public final class JavaFileAttach {

    @SneakyThrows
    public void append(File inputJarPath, String file) {

        var pathSeparators = file.split("/", -1);
        var name = pathSeparators[pathSeparators.length - 1];
        log.info("testing here wit name: " + name);

        try {
            File tempJarFile = File.createTempFile("tempJar", ".jar");
            tempJarFile.deleteOnExit();

            try (JarFile jarFile = new JarFile(inputJarPath);
                 FileOutputStream fos = new FileOutputStream(tempJarFile);
                 JarOutputStream jos = new JarOutputStream(fos)) {

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    jos.putNextEntry(new JarEntry(entry.getName()));
                    try (InputStream is = jarFile.getInputStream(entry)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            jos.write(buffer, 0, length);
                        }
                    }
                    jos.closeEntry();
                }

                log.info("platforms/" + file);
                try (InputStream is = Node.class.getClassLoader().getResourceAsStream("platforms/" + file)) {
                    jos.putNextEntry(new JarEntry(name));
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        jos.write(buffer, 0, length);
                    }
                    jos.closeEntry();
                }
            }

            File outputJarFile = new File(inputJarPath.getAbsolutePath());
            if (outputJarFile.exists()) {
                outputJarFile.delete();
            }
            tempJarFile.renameTo(outputJarFile);

        } catch (IOException e) {

        }
    }
}
