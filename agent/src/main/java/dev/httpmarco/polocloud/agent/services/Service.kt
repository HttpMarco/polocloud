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
    var maxPlayerCount =  -1

    fun name(): String {
        return "${group.data.name}-${id}"
    }

    fun shutdown() {
        Agent.instance.runtime.factory().shutdownApplication(this)
    }

    fun executeCommand(command: String) {
        Agent.instance.runtime.expender().executeCommand(this, command)
    }

    fun logs(limit: Int = 100): List<String> {
        return Agent.instance.runtime.expender().readLogs(this,limit)
    }

    enum class State {
        PREPARING,
        STARTING,
        ONLINE,
        STOPPING,
        STOPPED,
    }
}