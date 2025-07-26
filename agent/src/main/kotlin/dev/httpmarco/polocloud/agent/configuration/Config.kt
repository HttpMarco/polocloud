package dev.httpmarco.polocloud.agent.configuration

import dev.httpmarco.polocloud.agent.Agent

interface Config {

    fun save(path: String) {
        Agent.runtime.configHolder().write(path, this)
    }
}