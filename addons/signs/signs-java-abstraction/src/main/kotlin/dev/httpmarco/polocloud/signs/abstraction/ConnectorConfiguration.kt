package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.signs.abstraction.data.BasedConnectorData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout
import dev.httpmarco.polocloud.signs.abstraction.layout.ConnectorLayout
import dev.httpmarco.polocloud.v1.services.ServiceState
import kotlin.collections.listOf

class ConnectorConfiguration(
    val connectors: List<BasedConnectorData<*>> = listOf(),
    var layouts: List<ConnectorLayout<*>> = listOf(
        // default sign layout
        SignLayout(
            "default",
            mapOf(
                ServiceState.STARTING to listOf(
                    SignData.SignAnimationTick(
                        arrayOf("Starting", "Please wait..."),
                    )
                ),
                ServiceState.ONLINE to listOf(
                    SignData.SignAnimationTick(
                        arrayOf("Running", "All systems go!"),
                    )
                ),
                ServiceState.STOPPING to listOf(
                    SignData.SignAnimationTick(
                        arrayOf("Stopping", "See you soon!"),
                    )
                )
            )
        )
    )
)
