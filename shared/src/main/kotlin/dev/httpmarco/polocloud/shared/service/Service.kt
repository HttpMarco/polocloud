package dev.httpmarco.polocloud.shared.service

import dev.httpmarco.polocloud.shared.polocloudShared
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import dev.httpmarco.polocloud.v1.services.ServiceState

open class Service(
    val groupName: String,
    val id: Int,
    var state: ServiceState,
    val type: GroupType,
    var properties: Map<String, String>,
    val hostname: String,
    val port: Int,
    playerCount: Int,
    maxPlayerCount: Int,
    memoryUsage: Double,
    cpuUsage: Double
) {

    var maxPlayerCount: Int = maxPlayerCount
        protected set

    var playerCount: Int = playerCount
        protected set

    var memoryUsage: Double = memoryUsage
        protected set

    var cpuUsage: Double = cpuUsage
        protected set

    fun name(): String {
        return "${groupName}-${id}"
    }

    companion object {
        fun bindSnapshot(snapshot: ServiceSnapshot): Service {
            return Service(
                groupName = snapshot.groupName,
                id = snapshot.id,
                state = snapshot.state,
                type = snapshot.serverType,
                properties = snapshot.propertiesMap,
                hostname = snapshot.hostname,
                port = snapshot.port,
                maxPlayerCount = snapshot.maxPlayerCount,
                playerCount = snapshot.playerCount,
                memoryUsage = snapshot.memoryUsage,
                cpuUsage = snapshot.cpuUsage
            )
        }
    }

    fun toSnapshot(): ServiceSnapshot {
        return ServiceSnapshot.newBuilder()
            .setGroupName(groupName)
            .setId(id)
            .setState(state)
            .setServerType(type)
            .putAllProperties(properties)
            .setHostname(hostname)
            .setMaxPlayerCount(maxPlayerCount)
            .setPlayerCount(playerCount)
            .setPort(port)
            .build()
    }

    fun shutdown() {
        polocloudShared.serviceProvider().shutdownService(this.name())
    }
}