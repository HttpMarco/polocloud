package dev.httpmarco.polocloud.agent.module

import dev.httpmarco.polocloud.agent.utils.Reloadable
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.notExists

class ModuleFactory : Reloadable {

    private val modulePath = Path("local/modules")
    private val loadedModules = mutableListOf<LoadedModule>()

    init {
        if (this.modulePath.notExists()) {
            Files.createDirectories(this.modulePath)
        }
    }

    override fun reload() {
        TODO("Not yet implemented")
    }

    fun loadModules() {
        val (successful, failed) = jarFiles().mapNotNull { file ->
            val metadata = file.readMetadata() ?: return@mapNotNull null

            runCatching {

            }
        }
    }

    private fun loadModuleFromFile(file: File, metadata: ModuleMetadata): LoadedModule {
        val classLoader = URLClassLoader(arrayOf(file.toURI().toURL(), ))
    }

    private fun jarFiles() = this.modulePath.toFile().listFiles { _, name -> name.endsWith(".jar") }?.toList().orEmpty()

    private fun File.readMetadata(): ModuleMetadata? =
        runCatching {
            JarFile(this).use { jar ->
                val entry = jar.getEntryJar("module.json") ?: return null
                jar.getInputStream(entry).use { inputStream ->
                    JsonUtils.GSON.fromJson(stream.reader(), ModuleMetadata::class.java)
                }
            }
        }.onFailure {
            i18n.error("agent.module.metadata.read.failed", it.name)
        }.getOrNull()

}