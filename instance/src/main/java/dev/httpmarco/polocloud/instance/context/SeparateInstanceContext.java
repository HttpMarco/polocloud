package dev.httpmarco.polocloud.instance.context;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public final class SeparateInstanceContext extends InstanceContext {

    @Contract("_ -> new")
    @Override
    @SneakyThrows
    public @NotNull ClassLoader context(@NotNull File bootFile) {
        return new URLClassLoader(new URL[]{bootFile.toURI().toURL()}, ClassLoader.getSystemClassLoader());
    }
}
