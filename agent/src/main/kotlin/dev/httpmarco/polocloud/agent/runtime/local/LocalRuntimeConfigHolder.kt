package dev.httpmarco.polocloud.agent.runtime.local

import com.google.gson.GsonBuilder
import dev.httpmarco.polocloud.agent.configuration.Config
import dev.httpmarco.polocloud.agent.configuration.serializer.LocalSerializer
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder
import java.util.Locale
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class LocalRuntimeConfigHolder : RuntimeConfigHolder {

    private val gson = GsonBuilder().setPrettyPrinting().registerTypeAdapter(Locale::class.java, LocalSerializer()).create()

    override fun <T : Config> read(key: String, defaultValue: T): T {
        val target = Path("$key.json")

        if (target.exists()) {
            return gson.fromJson<T>(target.readText(Charsets.UTF_8), defaultValue.javaClass)
        }
        target.writeText(gson.toJson(defaultValue))
        return defaultValue
    }
}