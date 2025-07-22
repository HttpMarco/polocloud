package dev.httpmarco.polocloud.platforms

import dev.httpmarco.polocloud.platforms.exceptions.PlatformVersionInvalidException
import dev.httpmarco.polocloud.platforms.tasks.PlatformTask
import dev.httpmarco.polocloud.platforms.tasks.PlatformTaskPool
import kotlinx.serialization.Serializable
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.name
import kotlin.io.path.notExists

@Serializable
class Platform(
    val name: String,
    val url: String,
    val language: PlatformLanguage,
    // default is "stop"
    val shutdownCommand: String = "stop",
    // the type of platform, proxy, server, or service
    val type: PlatformType,
    // all global arguments for the platform after the jar name, for example, 'nogui'
    val arguments: List<String> = emptyList(),
    // all global flags for the platform after the jar name, for example: '-Djava.net.preferIPv4Stack=true'
    val flags: List<String> = emptyList(),
    // all versions of the platform that are supported
    val versions: List<PlatformVersion>,
    private val bridge: String? = null,
    // if the path is empty, the platform will not copy the bridge
    private val bridgePath: String? = null,
    // the tasks that should be run after the platform is prepared
    private val tasks: List<String> = emptyList(),
    // if true, the polocloud server icon will be copied to the service path
    private val copyServerIcon: Boolean = true
) {

    fun prepare(servicePath: Path, version: String, environment: Map<String, String>) {
        // This method should handle the preparation of the platform, such as downloading the necessary files
        // or setting up the environment for the specified version.
        // Implementation details would depend on the specific requirements of the platform.
        val path = Path("local/metadata/cache/$name/$version/$name-$version" + language.suffix())
        val version = this.version(version)

        if (version == null) {
            throw PlatformVersionInvalidException()
        }

        path.parent.createDirectories()

        if (path.notExists()) {
            URI(
                url.replace("%version%", version.version)
                    .replace("%buildId%", version.buildId?:"null")
            ).toURL().openStream().use { input ->
                path.toFile().outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        tasks().forEach { it.runTask(servicePath, environment) }

        // copy the platform file to the service path
        Files.copy(path, servicePath.resolve(path.name), StandardCopyOption.REPLACE_EXISTING)

        if (bridge != null) {
            // copy the bridge if present
            val sourceBridge = Path("local/libs/polocloud-${language.name.lowercase()}-bridge-${System.getenv("polocloud-version")}.jar")
            val targetBridge = servicePath.resolve(bridgePath + "/" + sourceBridge.name)

            targetBridge.parent.createDirectories()
            Files.copy(sourceBridge, targetBridge, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    fun version(version: String): PlatformVersion? {
        return versions.firstOrNull { it.version == version }
    }

    fun tasks(): List<PlatformTask> {
        return tasks.map { PlatformTaskPool.find(it)!! }.toList()
    }
}