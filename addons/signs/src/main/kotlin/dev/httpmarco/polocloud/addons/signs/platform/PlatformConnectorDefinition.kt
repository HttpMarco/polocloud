package dev.httpmarco.polocloud.addons.signs.platform

import dev.httpmarco.polocloud.addons.signs.Connectors

abstract class PlatformConnectorDefinition {

    init {
        // initialize the platform connector
        Connectors()
    }
}