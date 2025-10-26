package dev.httpmarco.polocloud.shared.service

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.httpmarco.polocloud.shared.polocloudShared
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.GroupType
import dev.httpmarco.polocloud.v1.services.ServiceSnapshot
import dev.httpmarco.polocloud.v1.services.ServiceState
import kotlin.collections.component1
import kotlin.collections.component2

open class Service(
    val groupName: String,
    val id: Int,
    var state: ServiceState,
    val type: GroupType,
    var properties: Map<String, String>,
    hostname: String,
    val port: Int,
    var templates: List<Template>,
    val information: ServiceInformation,
    minMemory: Int,
    maxMemory: Int,
    playerCount: Int = -1,
    maxPlayerCount: Int = -1,
    memoryUsage: Double = -1.0,
    cpuUsage: Double = -1.0,
    motd: String = ""
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

    var hostname: String = hostname
        protected set

    fun name(): String {
        return "${groupName}-${id}"
    }

    open fun changeState(state: ServiceState) {
        this.state = state

        // TODO update service in docker storage
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
                templates = Template.bindSnapshot(snapshot.templatesList),
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
            .addAllTemplates(templates.map { it.toSnapshot() })
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

fun Service.toJson(): JsonObject {
    val serviceTemplates = JsonArray().apply {
        templates.forEach { template ->
            add(JsonObject().apply {
                addProperty("name", template.name)
                addProperty("size", template.size())
            })
        }
    }

    val serviceProperties = JsonObject().apply {
        properties.forEach { (key, value) ->
            addProperty(key, value)
        }
    }

    return JsonObject().apply {
        addProperty("name", name())
        addProperty("state", state.name)
        addProperty("type", type.name)
        addProperty("groupName", groupName)
        addProperty("hostname", hostname)
        addProperty("port", port)
        add("templates", serviceTemplates)
        add("properties", serviceProperties)
        addProperty("minMemory", minMemory)
        addProperty("maxMemory", maxMemory)
        addProperty("playerCount", playerCount)
        addProperty("maxPlayerCount", maxPlayerCount)
        addProperty("memoryUsage", memoryUsage)
        addProperty("cpuUsage", cpuUsage)
        addProperty("motd", motd)
    }
}