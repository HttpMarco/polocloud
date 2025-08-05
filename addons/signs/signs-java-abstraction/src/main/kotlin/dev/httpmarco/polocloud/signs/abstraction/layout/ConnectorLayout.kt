package dev.httpmarco.polocloud.signs.abstraction.layout

import dev.httpmarco.polocloud.v1.services.ServiceState

abstract class ConnectorLayout<A : AnimationFrame>(val id: String, val frames: Map<ServiceState, List<A>>) {

    fun typeId(): String {
        return this::class.java.simpleName.replace("Layout", "").uppercase()
    }
}