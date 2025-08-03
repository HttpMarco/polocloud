package dev.httpmarco.polocloud.agent.services

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.agent.groups.AbstractGroup
import dev.httpmarco.polocloud.agent.utils.PortDetector
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.v1.services.ServiceState

abstract class AbstractService(val group: AbstractGroup, id: Int, hostname: String = "127.0.0.1") :

    Service(
        group.name,
        id,
        ServiceState.PREPARING,
        group.platform().type,
        hashMapOf(), hostname,
        PortDetector.nextPort(group),
        -1,
        -1,
        -1.0,
        -1.0
    ) {

    init {
        properties += group.properties.map { it.key to it.value.toString() }.toMap()
    }

    fun isStatic() : Boolean {
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

    fun updateMaxPlayerCount(maxPlayerCount: Int) {
        this.maxPlayerCount = maxPlayerCount
    }

    fun updatePlayerCount(playerCount: Int) {
        this.playerCount = playerCount
    }

    fun updateCpuUsage(cpuUsage: Double) {
        this.cpuUsage = cpuUsage
    }

    fun updateMemoryUsage(memoryUsage: Double) {
        this.memoryUsage = memoryUsage
    }
}