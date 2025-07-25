package dev.httpmarco.polocloud.agent.runtime

import dev.httpmarco.polocloud.agent.configuration.Config

interface RuntimeConfigHolder {

    fun <T : Config> read(key: String, defaultValue: T) : T

    fun <T : Config> write(key: String, value: T)

}