package dev.httpmarco.polocloud.suite;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.jar.JarFile;

public final class PolocloudContext {

    private static Instrumentation context;

    public static void defineContext(Instrumentation instrumentation) {
        context = instrumentation;
    }

    public static void attachPath(Path path) throws IOException {
        context.appendToSystemClassLoaderSearch(new JarFile(path.toFile()));
    }
}
