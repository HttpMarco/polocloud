package dev.httpmarco.polocloud.agent.bridges

import dev.httpmarco.polocloud.agent.bridges.BridgeType
import kotlinx.serialization.Serializable

@Serializable
data class Bridge(private val name: String, private val type: BridgeType) {





}