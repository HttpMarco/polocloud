package dev.httpmarco.polocloud.addons.api

import com.google.gson.GsonBuilder
import java.io.File

class ConfigFactory<T : Any>(
    private val clazz: Class<T>,
    private val folder: File? = null,
    fileName: String
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile = if (folder != null) File(folder, fileName) else File(fileName)

    var config: T = loadOrCreate()
        private set

    private fun loadOrCreate(): T {
        if (folder != null && !folder.exists()){
            folder.mkdirs()
        }

        if (!configFile.exists()) {
            val defaultInstance = clazz.getDeclaredConstructor().newInstance()
            save(defaultInstance)
            return defaultInstance
        }

        return runCatching {
            gson.fromJson(configFile.readText(Charsets.UTF_8), clazz)
        }.getOrNull() ?: clazz.getDeclaredConstructor().newInstance()
    }

    fun save(config: T = this.config) {
        configFile.writeText(gson.toJson(config), Charsets.UTF_8)
    }
}