package dev.httpmarco.polocloud.shared.module

import java.net.URLClassLoader

data class LoadedModule(
    val polocloudModule: PolocloudModule,
    val classLoader: URLClassLoader,
    val metadata: ModuleMetadata
)