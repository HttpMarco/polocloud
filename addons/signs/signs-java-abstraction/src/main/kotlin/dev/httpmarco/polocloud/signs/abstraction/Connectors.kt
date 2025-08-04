package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData

abstract class Connectors<M> {

    private val configurationFactory = ConfigFactory(ConnectorConfiguration::class.java, fileName = "connectors.json")

    private val supports = listOf<ConnectorSupport<M>>()

    private val connectors = configurationFactory.config.connectors.map {
        when (it) {
            is SignData -> generateSignConnector(it)
            is BannerData -> generateBannerConnector(it)
            else -> throw IllegalArgumentException("Unknown connector type: ${it::class.java}")
        }
    }

    abstract fun generateSignConnector(data: SignData): Connector<SignData.SignAnimationTick>

    abstract fun generateBannerConnector(data: BannerData): Connector<BannerData.BannerAnimationTick>

    fun isSupported(material: M): Boolean {
        return supports.any { it.isSupported(material) }
    }
}