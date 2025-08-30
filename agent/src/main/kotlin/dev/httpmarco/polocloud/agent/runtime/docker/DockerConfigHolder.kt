package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.configuration.Config
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder
import dev.httpmarco.polocloud.common.json.GSON
import java.io.File

class DockerConfigHolder : RuntimeConfigHolder {

    override fun <T : Config> read(fileName: String, defaultValue: T): T {
        val file = File("runtime/configs/$fileName")
        return if (file.exists()) {
            file.reader().use { reader ->
                GSON.fromJson(reader, defaultValue.javaClass)
            }
        } else {
            write(fileName, defaultValue)
            defaultValue
        }
    }

    override fun <T : Config> write(fileName: String, value: T) {
        val file = File("runtime/configs/$fileName")
        file.parentFile?.mkdirs()
        file.writer().use { writer ->
            GSON.toJson(value, writer)
        }
    }
}