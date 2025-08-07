package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.sdk.java.Polocloud
import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayoutSerializer
import dev.httpmarco.polocloud.signs.abstraction.layout.LayoutConfiguration
import java.util.UUID

/**
 * Manages all types of connectors (e.g., signs, banners) used to visually represent services.
 * Handles configuration loading, layout mapping, support registration, and dynamic connector updates.
 *
 * @param M the platform-specific material type (e.g., a Minecraft Material enum)
 */
abstract class Connectors<M> {

    // -- Internal configuration loaders
    private val layoutFactory by lazy {
        ConfigFactory(
            clazz = LayoutConfiguration::class.java,
            folder = PLUGIN_DIRECTORY,
            fileName = "signs-layout.json",
            adapters = arrayOf(Pair(ConnectorLayout::class.java, ConnectorLayoutSerializer()))
        )
    }

    private val configurationFactory by lazy {
        ConfigFactory(
            clazz = ConnectorConfiguration::class.java,
            folder = PLUGIN_DIRECTORY,
            fileName = "signs-connectors.json",
            adapters = arrayOf(Pair(BasedConnectorData::class.java, ConnectorSerializer(this)))
        )
    }

    /**
     * All loaded layouts available for visual connectors.
     */
    private val layouts = layoutFactory.config.layouts

    /**
     * List of platform-specific material supports.
     */
    private val supports = mutableListOf<ConnectorSupport<M, *>>()

    /**
     * All connectors currently managed by this instance.
     */
    private val connectors = configurationFactory.config.connectors.map {
        when (it) {
            is SignData -> generateSignConnector(it)
            is BannerData -> generateBannerConnector(it)
            else -> error("Unknown connector type: ${it::class.java.name}")
        }
    }.toMutableList()

    /**
     * Initializes the connector context and registers cloud events.
     * Updates all connectors after loading.
     */
    init {
        ConnectorCloudEvents(this)

        this.connectors.forEach { it.update() }

        Polocloud.instance().serviceProvider().findAll().forEach {
            if (it.name() == Polocloud.instance().selfServiceName()) {
                return@forEach
            }

            var possibleConnector = findEmptyConnector(it.groupName)
            possibleConnector?.bindWith(it)
        }
    }

    /**
     * Registers a support handler that defines how to interpret a given material type.
     *
     * @param support the support handler to register
     */
    fun bindSupport(support: ConnectorSupport<M, *>) {
        supports += support
    }

    /**
     * Checks whether a specific material is supported by any registered handler.
     *
     * @param material the material to check
     * @return true if supported, false otherwise
     */
    fun isSupported(material: M): Boolean {
        return supports.any { it.isSupported(material) }
    }

    /**
     * Searches for a layout by its identifier.
     *
     * @param layoutName the ID of the layout
     * @return the matching layout or null if not found
     */
    fun findLayout(layoutName: String): ConnectorLayout<*>? {
        return layouts.firstOrNull { it.id == layoutName }
    }

    /**
     * Attaches a new connector at a specific position and saves it to the configuration.
     *
     * @param group the group the connector belongs to
     * @param position the in-world position of the connector
     * @param material the material used to determine the support type
     */
    fun attachConnector(group: String, position: Position, material: M) {
        val support = supports.firstOrNull { it.isSupported(material) }
            ?: error("No support found for material: $material")

        val connector = support.handledConnector(group, position)
        connectors += connector

        configurationFactory.config.connectors += connector.basedConnectorData
        configurationFactory.save()

        connector.update()
    }

    /**
     * Creates and returns a sign layout by its name.
     *
     * @param layoutName the layout ID (default: "default")
     * @return the sign layout instance
     * @throws NoSuchElementException if not found
     */
    fun generateSignLayout(layoutName: String = "default"): SignLayout {
        return layouts.first {
            it.id == layoutName && it is SignLayout
        } as SignLayout
    }

    /**
     * Finds an unassigned connector within a group that has no associated service yet.
     *
     * @param group the group to search in
     * @return the first available connector, or null if none found
     */
    fun findEmptyConnector(group: String): Connector<out AnimationFrame>? {
        return connectors.firstOrNull {
            it.displayedService == null && it.basedConnectorData.group == group
        }
    }

    /**
     * Finds a connector at a specific position.
     * This method is used to locate a connector based on its in-world position.
     *
     * @param position the position to search for
     */
    fun find(position: Position): Connector<out AnimationFrame>? {
        return connectors.firstOrNull { it.basedConnectorData.position == position }
    }

    /**
     * Finds the connector currently displaying the given service.
     *
     * @param service the service to search for
     * @return the connector displaying the service, or null if not found
     */
    fun findAttachConnector(service: Service): Connector<out AnimationFrame>? {
        return connectors.firstOrNull { it.displayedService != null && it.displayedService!!.name() == service.name() }
    }

    /**
     * Creates a sign connector from the given sign data.
     *
     * @param data the sign configuration
     * @return the resulting connector
     */
    abstract fun generateSignConnector(data: SignData): Connector<SignData.SignAnimationTick>

    /**
     * Creates a banner connector from the given banner data.
     *
     * @param data the banner configuration
     * @return the resulting connector
     */
    abstract fun generateBannerConnector(data: BannerData): Connector<BannerData.BannerAnimationTick>


    /**
     * Connect a player to a specific connector by its UUID.
     * This method is used to establish a connection
     */
    abstract fun connect(uuid: UUID, connector: Connector<*>)
}
