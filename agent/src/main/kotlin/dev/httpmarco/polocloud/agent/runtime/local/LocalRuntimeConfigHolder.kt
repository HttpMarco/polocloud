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

    override fun <T : Config> read(fileName: String, defaultValue: T): T {
        val target = Path("$fileName.json")

        if (target.exists()) {
            val config = gson.fromJson(target.readText(Charsets.UTF_8), defaultValue.javaClass)
            this.write(fileName, config)
            return config
        }

        this.write(fileName, defaultValue)
        return defaultValue
    }

    override fun <T : Config> write(fileName: String, value: T) {
        val target = Path("$fileName.json")
        target.writeText(gson.toJson(value))
    }
}