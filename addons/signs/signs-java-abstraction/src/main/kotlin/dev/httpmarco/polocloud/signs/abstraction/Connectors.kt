package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.addons.api.ConfigFactory
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData

abstract class Connectors {

    private val configurationFactory = ConfigFactory(ConnectorConfiguration::class.java, fileName = "connectors.json")
    private val connectors = configurationFactory.config.connectors.map { mapData(it) }

    abstract fun mapData(data: BasedConnectorData): Connector

}