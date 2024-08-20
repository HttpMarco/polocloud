package dev.httpmarco.polocloud.instance.context;

import dev.httpmarco.polocloud.instance.ClusterPremain;
import lombok.SneakyThrows;

import java.io.File;
import java.util.jar.JarFile;

public final class DefaultInstanceContext extends InstanceContext {

    @Override
    @SneakyThrows
    public ClassLoader context(File bootFile) {
        ClusterPremain.INSTRUMENTATION.appendToSystemClassLoaderSearch(new JarFile(bootFile));
        return ClassLoader.getSystemClassLoader();
    }
}
