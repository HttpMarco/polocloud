package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.v1.services.ServiceState
import kotlin.collections.listOf

class ConnectorConfiguration(
    var connectors: List<BasedConnectorData<*>> = listOf()
)