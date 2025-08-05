package dev.httpmarco.polocloud.signs.abstraction.layout

import dev.httpmarco.polocloud.signs.abstraction.ConnectorState

abstract class ConnectorLayout<A : AnimationFrame>(val id: String, val frames: Map<ConnectorState, List<A>>) {

    fun typeId(): String {
        return this::class.java.simpleName.replace("Layout", "").uppercase()
    }
}