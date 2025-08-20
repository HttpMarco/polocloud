package dev.httpmarco.polocloud.agent.services

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.utils.PortDetector
import dev.httpmarco.polocloud.shared.events.definitions.ServiceChangePlayerCountEvent
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.shared.service.ServiceInformation
import dev.httpmarco.polocloud.v1.services.ServiceState

abstract class AbstractService(val group: AbstractGroup, id: Int, hostname: String = "127.0.0.1") :

    Service(
        group.name,
        id,
        ServiceState.PREPARING,
        group.platform().type,
        hashMapOf(),
        hostname,
        PortDetector.nextPort(group),
        group.templates,
        ServiceInformation(System.currentTimeMillis()),
        group.minMemory,
        group.maxMemory,
        -1,
        -1,
        -1.0,
        -1.0,
        ""
    ) {

    init {
        properties += group.properties.map { it.key to it.value.toString() }.toMap()
    }

    fun isStatic(): Boolean {
        return properties["static"]?.toBoolean() ?: false
    }

    fun shutdown(shutdownCleanUp: Boolean = true) {
        Agent.runtime.factory().shutdownApplication(this, shutdownCleanUp)
    }

    fun executeCommand(command: String): Boolean {
        return Agent.runtime.expender().executeCommand(this, command)
    }

    fun logs(limit: Int = 100): List<String> {
        return Agent.runtime.expender().readLogs(this, limit)
    }

    fun updateMinMemory(minMemory: Int) {
        if (state == ServiceState.STARTING || state == ServiceState.ONLINE) {
            throw IllegalStateException("Cannot update minMemory while service is starting or online")
        }
        this.minMemory = minMemory
    }

    fun updateMaxMemory(maxMemory: Int) {
        if (state == ServiceState.STARTING || state == ServiceState.ONLINE) {
            throw IllegalStateException("Cannot update minMemory while service is starting or online")
        }
        this.maxMemory = maxMemory
    }

    fun updateMaxPlayerCount(maxPlayerCount: Int) {
        this.maxPlayerCount = maxPlayerCount
    }

    fun updatePlayerCount(playerCount: Int) {
        val oldPlayerCount = this.playerCount
        this.playerCount = playerCount

        if (this.state == ServiceState.ONLINE && oldPlayerCount != playerCount) {
            Agent.eventProvider().call(ServiceChangePlayerCountEvent(this))
        }
    }

    fun updateMotd(motd: String) {
        this.motd = motd
    }

    fun updateCpuUsage(cpuUsage: Double) {
        this.cpuUsage = cpuUsage
    }

    fun updateMemoryUsage(memoryUsage: Double) {
        this.memoryUsage = memoryUsage
    }
}