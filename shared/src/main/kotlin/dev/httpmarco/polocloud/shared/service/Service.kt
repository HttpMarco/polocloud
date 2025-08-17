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
    var templates: List<String>,
    val information: ServiceInformation,
    minMemory: Int,
    maxMemory: Int,
    playerCount: Int,
    maxPlayerCount: Int,
    memoryUsage: Double,
    cpuUsage: Double,
    motd: String
) {


    var minMemory: Int = minMemory
        protected set

    var maxMemory: Int = maxMemory
        protected set

    var maxPlayerCount: Int = maxPlayerCount
        protected set

    var playerCount: Int = playerCount
        protected set

    var memoryUsage: Double = memoryUsage
        protected set

    var cpuUsage: Double = cpuUsage
        protected set

    var motd: String = motd
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
                templates = snapshot.templatesList,
                information = ServiceInformation.bindSnapshot(snapshot.information),
                minMemory = snapshot.minimumMemory,
                maxMemory = snapshot.maximumMemory,
                maxPlayerCount = snapshot.maxPlayerCount,
                playerCount = snapshot.playerCount,
                memoryUsage = snapshot.memoryUsage,
                cpuUsage = snapshot.cpuUsage,
                motd = snapshot.motd
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
            .addAllTemplates(templates)
            .setInformation(information.toSnapshot())
            .setMinimumMemory(minMemory)
            .setMaximumMemory(maxMemory)
            .setMaxPlayerCount(maxPlayerCount)
            .setPlayerCount(playerCount)
            .setPort(port)
            .setMotd(motd)
            .build()
    }

    fun shutdown() {
        polocloudShared.serviceProvider().shutdownService(this.name())
    }

    override fun equals(other: Any?): Boolean =
        other is Service &&
                groupName == other.groupName &&
                id == other.id &&
                state == other.state &&
                type == other.type &&
                properties == other.properties &&
                hostname == other.hostname &&
                port == other.port

    override fun hashCode(): Int =
        listOf(groupName, id, state, type, properties, hostname, port).hashCode()
}