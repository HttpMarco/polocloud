package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.platform.PlatformModel

data class GroupCreateModel(
    val name: String = "",
    val minMemory: Int = 0,
    val maxMemory: Int = 0,
    val minOnlineService: Int = 0,
    val maxOnlineService: Int = 0,
    val platform: PlatformModel = PlatformModel(),
    val percentageToStartNewService: Double = 0.0,
    val createdAt: Long = 0L,
    val templates: List<String> = emptyList(),
    val properties: MutableMap<String, JsonPrimitive> = mutableMapOf()
)