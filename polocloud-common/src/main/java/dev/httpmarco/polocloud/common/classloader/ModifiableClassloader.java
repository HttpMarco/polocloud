package dev.httpmarco.polocloud.common.classloader;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class ModifiableClassloader extends URLClassLoader {

    public ModifiableClassloader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @SneakyThrows
    public void add(File file) {
        super.addURL(file.toURI().toURL());
    }
}
