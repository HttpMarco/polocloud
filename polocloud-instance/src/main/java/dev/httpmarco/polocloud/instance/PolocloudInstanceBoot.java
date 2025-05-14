package dev.httpmarco.polocloud.instance;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.jar.JarFile;

public final class PolocloudInstanceBoot {

    public static void main(String[] args) {
        new PolocloudInstance(args);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        try {
            instrumentation.appendToSystemClassLoaderSearch(new JarFile(Path.of("../../local/libs/polocloud-instance-2.0.0.jar").toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
