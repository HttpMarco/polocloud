package dev.httpmarco.polocloud.shared.groups

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.shared.template.Template
import dev.httpmarco.polocloud.v1.groups.GroupSnapshot
import kotlin.collections.component1
import kotlin.collections.component2

open class Group(
    val name: String,
    minMemory: Int,
    maxMemory: Int,
    minOnlineService: Int,
    maxOnlineService: Int,
    val platform: PlatformIndex,
    percentageToStartNewService: Double,
    val createdAt: Long,
    val templates: List<Template>,
    var properties: Map<String, JsonPrimitive>
) {

    var minMemory: Int = minMemory
        protected set

    var maxMemory: Int = maxMemory
        protected set

    var minOnlineService: Int = minOnlineService
        protected set

    var maxOnlineService: Int = maxOnlineService
        protected set

    var percentageToStartNewService: Double = percentageToStartNewService
        protected set

    companion object {
        fun bindSnapshot(snapshot: GroupSnapshot): Group {
            return Group(
                snapshot.name,
                snapshot.minimumMemory,
                snapshot.maximumMemory,
                snapshot.minimumOnline,
                snapshot.maximumOnline,
                PlatformIndex(snapshot.platform.name, snapshot.platform.version),
                snapshot.percentageToStartNewService,
                snapshot.createdAt,
                Template.bindSnapshot(snapshot.templatesList),
                snapshot.propertiesMap.map { it.key to JsonPrimitive(it.value) }.toMap()
            )
        }
    }

    fun toSnapshot(): GroupSnapshot {
        return GroupSnapshot.newBuilder()
            .setName(name)
            .setMinimumMemory(minMemory)
            .setMaximumMemory(maxMemory)
            .setMinimumOnline(minOnlineService)
            .setMaximumOnline(maxOnlineService)
            .setPlatform(platform.toSnapshot())
            .setPercentageToStartNewService(percentageToStartNewService)
            .setCreatedAt(createdAt)
            .addAllTemplates(templates.map { it.toSnapshot() })
            .putAllProperties(properties.map { it.key to it.value.toString() }.toMap())
            .build()
    }
}

fun Group.toJson(): JsonObject {
    val groupPlatform = JsonObject().apply {
        addProperty("name", platform.name)
        addProperty("version", platform.version)
    }

    val groupTemplates = JsonArray().apply {
        templates.forEach { template ->
            add(JsonObject().apply {
                addProperty("name", template.name)
                addProperty("size", template.size())
            })
        }
    }

    val groupProperties = JsonObject().apply {
        properties.forEach { (key, value) ->
            add(key, value)
        }
    }

    return JsonObject().apply {
        addProperty("name", name)
        addProperty("minMemory", minMemory)
        addProperty("maxMemory", maxMemory)
        addProperty("minOnlineService", minOnlineService)
        addProperty("maxOnlineService", maxOnlineService)
        add("platform", groupPlatform)
        addProperty("percentageToStartNewService", percentageToStartNewService)
        addProperty("createdAt", createdAt)
        add("templates", groupTemplates)
        add("groupProperties", groupProperties)
    }
}