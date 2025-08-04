package dev.httpmarco.polocloud.signs.abstraction

interface ConnectorSupport<M> {

    fun isSupported(material: M): Boolean

}