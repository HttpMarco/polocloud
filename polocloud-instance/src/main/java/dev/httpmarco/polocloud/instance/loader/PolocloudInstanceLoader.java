package dev.httpmarco.polocloud.instance.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class PolocloudInstanceLoader extends URLClassLoader {

    public PolocloudInstanceLoader(Path path) throws MalformedURLException {
        super(new URL[]{path.toFile().toURI().toURL()}, ClassLoader.getSystemClassLoader());
    }
}
