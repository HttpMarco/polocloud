package dev.httpmarco.polocloud.common;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class ModifiableClassloader extends URLClassLoader {

    public ModifiableClassloader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @SneakyThrows
    public void attach(String file) {
        super.addURL(new File(file).toURI().toURL());
    }
}
