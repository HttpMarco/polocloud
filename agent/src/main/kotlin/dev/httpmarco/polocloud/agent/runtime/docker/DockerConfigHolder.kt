package dev.httpmarco.polocloud.agent.runtime.docker

import dev.httpmarco.polocloud.agent.configuration.Config
import dev.httpmarco.polocloud.agent.runtime.RuntimeConfigHolder

class DockerConfigHolder : RuntimeConfigHolder {

    override fun <T : Config> read(
        fileName: String,
        defaultValue: T
    ): T {
        TODO("Not yet implemented")
    }

    override fun <T : Config> write(fileName: String, value: T) {
        TODO("Not yet implemented")
    }
}