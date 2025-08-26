package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group

import com.google.gson.JsonPrimitive
import dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.platform.PlatformModel
import dev.httpmarco.polocloud.shared.template.Template

data class GroupCreateModel(
    val name: String = "",
    val minMemory: Int = 0,
    val maxMemory: Int = 0,
    val minOnlineService: Int = 0,
    val maxOnlineService: Int = 0,
    val platform: PlatformModel = PlatformModel(),
    val percentageToStartNewService: Double = 0.0,
    val information: GroupInformationModel = GroupInformationModel(),
    val templates: List<Template> = emptyList(),
    val properties: Map<String, JsonPrimitive> = emptyMap()
)