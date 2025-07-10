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
    val type: PlatformType,
    val arguments: List<String>,
    val versions: List<PlatformVersion>,
    val bridgePath: String = "",
    private val tasks: List<String>
) {

    fun prepare(servicePath: Path, version: String, environment: Map<String, String>) {
        // This method should handle the preparation of the platform, such as downloading the necessary files
        // or setting up the environment for the specified version.
        // Implementation details would depend on the specific requirements of the platform.
        val path = Path("local/metadata/cache/$name/$version/$name-$version.jar")
        val version = this.version(version)

        if (version == null) {
            throw PlatformVersionInvalidException()
        }

        path.parent.createDirectories()

        if (path.notExists()) {
            URI(
                url.replace("%version%", version.version)
                    .replace("%buildId%", version.buildId)
            ).toURL().openStream().use { input ->
                path.toFile().outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        tasks().forEach { it.runTask(servicePath, environment) }

        // copy the platform file to the service path
        Files.copy(path, servicePath.resolve(path.name), StandardCopyOption.REPLACE_EXISTING)

        if (environment["need-bridge"]?.toBoolean() == true) {
            // copy the bridge if present
            val sourceBridge = Path("local/libs/polocloud-${language.name.lowercase()}-bridge-3.0.0.BETA.jar")
            val targetbridge = Path(bridgePath + "/" + sourceBridge.name)

            targetbridge.parent.createDirectories()
            Files.copy(sourceBridge, targetbridge, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    fun version(version: String): PlatformVersion? {
        return versions.firstOrNull { it.version == version }
    }

    fun tasks(): List<PlatformTask> {
        return tasks.map { PlatformTaskPool.find(it)!! }.toList()

    }
}