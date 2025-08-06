package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayoutSerializer
import dev.httpmarco.polocloud.signs.abstraction.layout.LayoutConfiguration
import java.io.File

abstract class Connectors<M> {

    companion object {
        lateinit var context: Connectors<*>
    }

    private val configurationFactory =
        ConfigFactory(
            clazz = ConnectorConfiguration::class.java,
            folder = File("plugins/polocloud"),
            fileName = "signs-connectors.json"
        )

    private val layoutFactory = ConfigFactory(
        clazz = LayoutConfiguration::class.java,
        folder = File("plugins/polocloud"),
        fileName = "signs-layout.json",
        adapters = arrayOf(Pair(ConnectorLayout::class.java, ConnectorLayoutSerializer()))
    )

    private val layouts = layoutFactory.config.layouts
    private var supports = listOf<ConnectorSupport<M, *>>()

    private var connectors = configurationFactory.config.connectors.map {
        when (it) {
            is SignData -> generateSignConnector(it)
            is BannerData -> generateBannerConnector(it)
            else -> throw IllegalArgumentException("Unknown connector type: ${it::class.java}")
        }
    }

    init {
        context = this

        ConnectorCloudEvents()
    }

    fun bindSupport(support: ConnectorSupport<M, *>) {
        supports += support
    }

    abstract fun generateSignConnector(data: SignData): Connector<SignData.SignAnimationTick>

    abstract fun generateBannerConnector(data: BannerData): Connector<BannerData.BannerAnimationTick>

    fun isSupported(material: M): Boolean {
        return supports.any { it.isSupported(material) }
    }

    fun attachConnector(group: String, position: Position, material: M) {
        // detect with the block material which support is handling this
        val bindSupport = supports.first { it.isSupported(material) }

        val connector = bindSupport.handledConnector(group, position)
        connectors += connector

        // display the first stream
        connector.update()
    }

    fun generateSignLayout(layoutName: String = "default"): SignLayout {
        return layouts.first {
            it.id == layoutName && it is SignLayout
        } as SignLayout
    }

    fun findEmptyConnector(group: String): Connector<out AnimationFrame>? {
        return connectors.firstOrNull { it.displayedService == null && it.basedConnectorData.group == group }
    }

    fun findAttachConnector(service: Service): Connector<*>? {
        return connectors.firstOrNull { it.displayedService == service }
    }
}