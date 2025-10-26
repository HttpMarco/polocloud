package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.configuration.Config

interface RuntimeConfigHolder {

    fun <T : Config> read(fileName: String, defaultValue: T) : T

    fun <T : Config> write(fileName: String, value: T)

}