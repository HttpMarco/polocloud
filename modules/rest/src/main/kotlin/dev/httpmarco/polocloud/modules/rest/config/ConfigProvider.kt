package dev.httpmarco.polocloud.modules.rest.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class ConfigProvider(
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
) {

    fun <T : Config> read(key: String, defaultValue: T): T {
        val target = Path("$key.json")

        if (target.exists()) {
            val config = gson.fromJson(target.readText(Charsets.UTF_8), defaultValue.javaClass)
            this.write(key, config)
            return config
        }

        this.write(key, defaultValue)
        return defaultValue
    }

    fun <T : Config> write(key: String, value: T) {
        val target = Path("$key.json")
        target.writeText(gson.toJson(value))
    }
}