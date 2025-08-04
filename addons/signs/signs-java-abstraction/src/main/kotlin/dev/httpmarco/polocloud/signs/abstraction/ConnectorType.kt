package dev.httpmarco.polocloud.signs.abstraction

interface ConnectorType<T : ConnectorFrame, M> {

    fun isSupported(material : M): Boolean

}