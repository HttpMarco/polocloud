package dev.httpmarco.polocloud.addons.hub

import com.google.gson.GsonBuilder
import java.io.File

class ConfigFactory(dataFolder: File) {
    private val configFile = File(dataFolder, "hub-config.json")
    private val gson = GsonBuilder().setPrettyPrinting().create()
    var config: HubConfig = HubConfiguration().serialize()
            private set

    init {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        loadOrCreateConfig()
    }

    private fun loadOrCreateConfig() {
        if (!this.configFile.exists()) {
            val default = HubConfiguration()
            this.config = default.serialize()

            saveConfig(default)
            return
        }

        runCatching {
            val loaded = gson.fromJson(configFile.readText(Charsets.UTF_8), HubConfiguration::class.java)
            val merged = HubConfiguration().mergeWith(loaded ?: HubConfiguration())

            this.config = merged.serialize()
            saveConfig(merged)
        }.onFailure {
            println("Failed to load hub configuration: ${it.message}")
            this.config = HubConfiguration().serialize()
        }
    }


    fun saveConfig(configuration: HubConfiguration = (config as HubSerializer).config) {
        runCatching {
            this.configFile.parentFile?.mkdirs()
            this.configFile.writeText(gson.toJson(configuration), Charsets.UTF_8)
        }.onFailure {
            println("Failed to save hub configuration: ${it.message}")
        }
    }
}