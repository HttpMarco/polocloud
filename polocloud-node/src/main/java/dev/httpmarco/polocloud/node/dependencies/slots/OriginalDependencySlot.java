package dev.httpmarco.polocloud.node.dependencies.slots;

import dev.httpmarco.polocloud.common.classpath.ModifiableClassloader;
import dev.httpmarco.polocloud.node.NodeBootContext;
import dev.httpmarco.polocloud.node.dependencies.Dependency;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.jar.JarFile;

public final class OriginalDependencySlot extends ClassloaderDependencySlot{

    public OriginalDependencySlot() {
        super(ModifiableClassloader.defaultClassLoader());
    }

    @Override
    @SneakyThrows
    public void rawBind(@NotNull Dependency file) {
        NodeBootContext.instrumentation().appendToSystemClassLoaderSearch(new JarFile(file.file()));
    }
}
