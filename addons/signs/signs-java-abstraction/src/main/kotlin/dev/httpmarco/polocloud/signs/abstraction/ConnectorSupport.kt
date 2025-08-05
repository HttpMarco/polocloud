package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.addons.api.location.Position
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout

interface ConnectorSupport<M, L : ConnectorLayout<*>> {

    fun isSupported(material: M): Boolean

    fun handledConnector(position: Position) : Connector<*>

}