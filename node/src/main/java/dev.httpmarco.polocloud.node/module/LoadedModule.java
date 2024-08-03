package dev.httpmarco.polocloud.node.module;

import java.net.URLClassLoader;

public record LoadedModule(CloudModule cloudModule, URLClassLoader moduleClassLoader, ModuleMetadata metadata) {
}