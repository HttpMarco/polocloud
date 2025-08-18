package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.shared.groups.GroupInformation
import dev.httpmarco.polocloud.shared.platform.PlatformIndex

data class GroupCreateModel(
    val name: String = "",
    val minMemory: Int = 0,
    val maxMemory: Int = 0,
    val minOnlineService: Int = 0,
    val maxOnlineService: Int = 0,
    val platform: PlatformIndex = PlatformIndex("", ""),
    val percentageToStartNewService: Double = 0.0,
    val information: GroupInformation = GroupInformation(System.currentTimeMillis()),
    val templates: List<String> = emptyList(),
    val properties: Map<String, JsonPrimitive> = emptyMap()
)