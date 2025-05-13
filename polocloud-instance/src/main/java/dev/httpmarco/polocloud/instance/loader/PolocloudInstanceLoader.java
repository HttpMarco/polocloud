package dev.httpmarco.polocloud.instance.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;

public final class PolocloudInstanceLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public PolocloudInstanceLoader(ClassLoader parent, Path... paths) {
        super(Arrays.stream(paths).map(it -> {
            try {
                return it.toUri().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(URL[]::new), parent);
    }

    public static PolocloudInstanceLoader generate(Path path, boolean separate) throws MalformedURLException {
        if(separate) {
            return new PolocloudInstanceLoader(ClassLoader.getPlatformClassLoader(), path,  Path.of("../../local/libs/polocloud-instance-2.0.0.jar"));
        }
        return new PolocloudInstanceLoader(ClassLoader.getSystemClassLoader(), path);
    }
}
