package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame

abstract class Connector<A : AnimationFrame>(val basedConnectorData: BasedConnectorData<A>) {

}