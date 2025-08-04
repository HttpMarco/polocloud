package dev.httpmarco.polocloud.signs.abstraction.data.sign

import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.v1.services.ServiceState

class SignLayout(id: String,  frames: Map<ServiceState, List<SignData.SignAnimationTick>>) : ConnectorLayout<SignData.SignAnimationTick>(id, frames) {

}