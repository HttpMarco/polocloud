package dev.httpmarco.polocloud.signs.abstraction.types.signs

import dev.httpmarco.polocloud.signs.abstraction.ConnectorType

abstract class AbstractSignConnectorType<M> : ConnectorType<SignFrame, M> {

    abstract fun display(frame : SignFrame)

    abstract fun destroy()

}