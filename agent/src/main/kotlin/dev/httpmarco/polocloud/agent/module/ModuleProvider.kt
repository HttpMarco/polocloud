package dev.httpmarco.polocloud.agent.module

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.utils.Reloadable
import dev.httpmarco.polocloud.common.json.GSON
import dev.httpmarco.polocloud.shared.module.LoadedModule
import dev.httpmarco.polocloud.shared.module.ModuleMetadata
import dev.httpmarco.polocloud.shared.module.PolocloudModule
import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.jar.JarFile
import kotlin.io.path.Path
import kotlin.io.path.notExists

class ModuleProvider : Reloadable {

    private val modulePath = Path("local/modules")
    private val loadedModules = mutableListOf<LoadedModule>()

    init {
        if (this.modulePath.notExists()) {
            Files.createDirectories(this.modulePath)
        }
    }

    override fun reload() {
        unloadModules()
        loadModules()
    }

    fun loadModules() {
        val (successful, failed) = jarFiles().mapNotNull { file ->
            val metadata = file.readMetadata() ?: return@mapNotNull null

            runCatching {
                loadModuleFromFile(file, metadata).also {
                    it.polocloudModule.onEnable()
                }
            }.fold(
                onSuccess = {
                    metadata.name to true
                },
                onFailure = {
                    i18n.error("agent.module.load.failed", metadata.id)
                    it.printStackTrace()
                    metadata.name to false
                }
            )
        }.partition { it.second }

        val status = successful.map { "&3${it.first}" } + failed.map { "&c${it.first}" }
        if (status.isNotEmpty()) {
            i18n.info( "agent.module.load.successful", status.joinToString("&8, "))
        }
    }

    fun unloadModules() {
        this.loadedModules.forEach { module ->
            runCatching {
                module.polocloudModule.onDisable()
                module.classLoader.close()
            }.onSuccess {
                i18n.info("agent.module.unload.successful", module.metadata.id)
            }.onFailure {
                i18n.warn("agent.module.unload.failed", module.metadata.id, it)
            }
        }
        this.loadedModules.clear()
    }

    private fun loadModuleFromFile(file: File, metadata: ModuleMetadata): LoadedModule {
        val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()), this::class.java.classLoader)
        val clazz = classLoader.loadClass(metadata.main)

        require(PolocloudModule::class.java.isAssignableFrom(clazz)) {
            i18n.error("agent.module.implementation.missing", metadata.id)
        }

        val instance = clazz.getDeclaredConstructor().newInstance() as PolocloudModule
        return LoadedModule(instance, classLoader, metadata).also {
            loadedModules += it
        }
    }

    private fun jarFiles() = this.modulePath.toFile().listFiles { _, name -> name.endsWith(".jar") }?.toList().orEmpty()

    private fun File.readMetadata(): ModuleMetadata? =
        runCatching {
            JarFile(this).use { jar ->
                val entry = jar.getJarEntry("module.json") ?: return null
                jar.getInputStream(entry).use { stream ->
                    GSON.fromJson(stream.reader(), ModuleMetadata::class.java)
                }
            }
        }.onFailure {
            i18n.error("agent.module.metadata.read.failed", name)
        }.getOrNull()

}