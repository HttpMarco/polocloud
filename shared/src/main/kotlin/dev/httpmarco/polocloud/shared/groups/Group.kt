package dev.httpmarco.polocloud.shared.groups

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.shared.platform.PlatformIndex
import dev.httpmarco.polocloud.v1.groups.GroupSnapshot

open class Group(
    val name: String,
    minMemory: Int,
    maxMemory: Int,
    minOnlineService: Int,
    maxOnlineService: Int,
    val platform: PlatformIndex,
    percentageToStartNewService: Double,
    val information: GroupInformation,
    val templates: List<String>,
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
                GroupInformation.bindSnapshot(snapshot.information),
                snapshot.templatesList,
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
            .setInformation(information.toSnapshot())
            .addAllTemplates(templates)
            .putAllProperties(properties.map { it.key to it.value.toString() }.toMap())
            .build()
    }

}