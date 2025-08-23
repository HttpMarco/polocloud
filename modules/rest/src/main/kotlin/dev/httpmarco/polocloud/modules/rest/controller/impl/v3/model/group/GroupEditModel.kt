package dev.httpmarco.polocloud.modules.rest.controller.impl.v3.model.group

data class GroupEditModel(
    val minMemory: Int = 0,
    val maxMemory: Int = 0,
    val minOnlineService: Int = 0,
    val maxOnlineService: Int = 0,
    val percentageToStartNewService: Double = 0.0,
)