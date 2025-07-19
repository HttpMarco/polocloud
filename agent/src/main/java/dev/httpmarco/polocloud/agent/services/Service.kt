package dev.httpmarco.polocloud.agent.services

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.Group
import dev.httpmarco.polocloud.agent.utils.PortDetector
import java.util.UUID

abstract class Service(val group: Group, val id: Int) {

    val port = PortDetector.nextPort(group)
    val hostname = "127.0.0.1"

    var state = State.PREPARING
    var playerCount = -1
    var maxPlayerCount = -1

    var properties = hashMapOf<String, String>()


    init {
        properties.putAll(group.data.properties)
    }

    fun name(): String {
        return "${group.data.name}-${id}"
    }

    fun shutdown(shutdownCleanUp : Boolean = true) {
        Agent.instance.runtime.factory().shutdownApplication(this, shutdownCleanUp)
    }

    fun executeCommand(command: String): Boolean {
        return Agent.instance.runtime.expender().executeCommand(this, command)
    }

    fun logs(limit: Int = 100): List<String> {
        return Agent.instance.runtime.expender().readLogs(this, limit)
    }

    enum class State {
        PREPARING,
        STARTING,
        ONLINE,
        STOPPING,
        STOPPED,
    }
}