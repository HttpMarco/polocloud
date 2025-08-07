package dev.httpmarco.polocloud.bridges.fabric.config

import com.google.gson.Gson
import java.io.File

object ConfigLoader {
    private val gson = Gson()
    private val configFile = File("fabric-bridge.json")

    fun load(): FabricBridgeConfig {
        if (!configFile.exists()) {
            val defaultConfig = FabricBridgeConfig(velocitySecret = "your-token-here")
            save(defaultConfig)
            return defaultConfig
        }

        return gson.fromJson(configFile.readText(), FabricBridgeConfig::class.java)
    }

    private fun save(config: FabricBridgeConfig) {
        configFile.writeText(gson.toJson(config))
    }
}