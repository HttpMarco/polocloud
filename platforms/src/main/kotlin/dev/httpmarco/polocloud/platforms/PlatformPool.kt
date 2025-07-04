package dev.httpmarco.polocloud.platforms

import dev.httpmarco.polocloud.common.json.PRETTY_JSON
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

private const val PLATFORM_TABLE_URL = "https://raw.githubusercontent.com/HttpMarco/polocloud/refs/heads/master/metadata/metadata.json"

@Serializable
class PlatformPool(val platforms: List<Platform>, val tasks: List<PlatformTask>, val path: Path) {

    companion object {
        private fun loadPlatform(url: String, name: String, type: String): Platform {
            return Json.decodeFromString<Platform>(URI("$url/$type/$name.json").toURL().readText())
        }

        fun load(path: Path): PlatformPool {
            if (path.exists()) {
                return PlatformPool(PRETTY_JSON.decodeFromString<List<Platform>>(Files.readString(path)), listOf(), path)
            } else {
                val platformTable: PlatformTable = Json.decodeFromString<PlatformTable>(URI(PLATFORM_TABLE_URL).toURL().readText())
                val platforms = mutableListOf<Platform>()
                val platformUrl = PLATFORM_TABLE_URL.substringBeforeLast("/")

                // first load all tasks, because all platforms need this
                platformTable.availableTask.forEach {

                }

                platformTable.availableProxies.forEach { platforms.add(loadPlatform(platformUrl, it, "proxies")) }
                platformTable.availableServers.forEach { platforms.add(loadPlatform(platformUrl, it, "servers")) }

                val platformPool = PlatformPool(platforms, listOf(), path)
                platformPool.saveLocal()

                return platformPool
            }
        }
    }

    fun saveLocal() {
        Files.writeString(path, PRETTY_JSON.encodeToString(platforms))
    }

    fun findPlatform(name: String): Platform? {
        return platforms.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
