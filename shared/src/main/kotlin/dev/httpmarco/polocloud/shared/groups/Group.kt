package dev.httpmarco.polocloud.shared.groups

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.shared.properties.PropertyHolder
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
        val properties: PropertyHolder
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

            val propertyHolder = PropertyHolder.empty()

            // Load properties from snapshot
            snapshot.propertiesMap.forEach { (key, value) ->
                propertyHolder.raw(
                        key,
                        when {
                            value.lowercase().toBooleanStrictOrNull() != null ->
                                    JsonPrimitive(value.toBoolean())
                            value.toIntOrNull() != null -> JsonPrimitive(value.toInt())
                            value.toDoubleOrNull() != null -> JsonPrimitive(value.toDouble())
                            value.toFloatOrNull() != null -> JsonPrimitive(value.toFloat())
                            else -> JsonPrimitive(value)
                        }
                )
            }

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
                    propertyHolder
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
                .putAllProperties(properties.all().mapValues { it.value.toString() })
                .build()
    }
}

fun Group.toJson(): JsonObject {
    val groupPlatform =
            JsonObject().apply {
                addProperty("name", platform.name)
                addProperty("version", platform.version)
            }

    val groupTemplates =
            JsonArray().apply {
                templates.forEach { template ->
                    add(
                        template.name
//                            JsonObject().apply {
//                                addProperty("name", template.name)
//                                addProperty("size", template.size())
//                            }
                    )
                }
            }

    val groupProperties =
            JsonObject().apply { properties.all().forEach { (key, value) -> add(key, value) } }

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
        add("properties", groupProperties)
    }
}
