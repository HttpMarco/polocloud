package dev.httpmarco.polocloud.launcher;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class PoloCloudLoader extends URLClassLoader {

    public PoloCloudLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @SneakyThrows
    public void addURL(@NotNull File file) {
        super.addURL(file.toURI().toURL());
    }
}
