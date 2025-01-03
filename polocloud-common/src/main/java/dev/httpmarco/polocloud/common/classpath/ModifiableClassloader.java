package dev.httpmarco.polocloud.common.classpath;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class ModifiableClassloader extends URLClassLoader {

    public ModifiableClassloader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @SneakyThrows
    public void attach(File file) {
        super.addURL(file.toURI().toURL());
    }

    public static ModifiableClassloader defaultClassLoader() {
        return new ModifiableClassloader();
    }
}
