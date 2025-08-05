package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.shared.service.Service
import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.layout.AnimationFrame

abstract class Connector<A : AnimationFrame>(val basedConnectorData: BasedConnectorData<A>) {

    var displayedService: Service? = null
    var state = this.detectState()

    abstract fun display(frame: A)

    fun bindWith(service: Service) {
        this.displayedService = service
        this.update()
    }

    init {
        this.update()
    }

    fun update() {
        this.state = this.detectState()

        // todo use right animation frame here
        this.display(basedConnectorData.layout.frames[state]!![0])
    }

    private fun detectState(): ConnectorState {
        return if (displayedService == null) {
            ConnectorState.LOADING
        } else if (displayedService!!.playerCount == 0) {
            ConnectorState.ONLINE
        } else if (displayedService!!.playerCount > 0) {
            ConnectorState.PLAYERS
        } else {
            ConnectorState.FULL
        }
    }
}