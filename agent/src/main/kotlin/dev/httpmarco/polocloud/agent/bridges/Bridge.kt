package dev.httpmarco.polocloud.agent.bridges

import dev.httpmarco.polocloud.agent.bridges.BridgeType
import kotlinx.serialization.Serializable

@Serializable
data class Bridge(val name: String, val type: BridgeType) {





}