package dev.httpmarco.polocloud.agent.groups

import dev.httpmarco.polocloud.platforms.PlatformIndex
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GroupData(
    val name: String,
    var platform: PlatformIndex,
    var minMemory: Int,
    var maxMemory: Int,
    var minOnlineService: Int,
    var maxOnlineService: Int,
    var percentageToStartNewService: Int,
    var templates: List<String>,
    var properties: Map<String, JsonElement>
)
