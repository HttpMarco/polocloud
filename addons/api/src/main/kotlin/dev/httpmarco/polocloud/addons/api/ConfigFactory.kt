package dev.httpmarco.polocloud.addons.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class ConfigFactory<T : Any>(
    private val clazz: Class<T>,
    private val folder: File? = null,
    fileName: String,
    vararg adapters: Pair<Class<*>, Any>
) {
    private val gson : Gson
    private val configFile = if (folder != null) File(folder, fileName) else File(fileName)

    init {
        val gson = GsonBuilder().setPrettyPrinting()

        adapters.forEach { (type, adapter) ->
            gson.registerTypeAdapter(type, adapter)
        }
        this.gson = gson.create()
    }

    var config: T = loadOrCreate()
        private set

    private fun loadOrCreate(): T {
        if (folder != null && !folder.exists()) {
            folder.mkdirs()
        }

        if (!configFile.exists()) {
            val defaultInstance = clazz.getDeclaredConstructor().newInstance()
            save(defaultInstance)
            return defaultInstance
        }

        return gson.fromJson(configFile.readText(Charsets.UTF_8), clazz)
    }

    fun save(config: T = this.config) {
        configFile.writeText(gson.toJson(config), Charsets.UTF_8)
    }
}