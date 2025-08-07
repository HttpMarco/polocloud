package dev.httpmarco.polocloud.signs.abstraction.layout

import dev.httpmarco.polocloud.signs.abstraction.ConnectorState
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignLayout

class LayoutConfiguration(
    var layouts: List<ConnectorLayout<*>> = listOf(
        // default sign layout
        SignLayout(
            "default",
            mapOf(
                ConnectorState.LOADING to listOf(
                    SignData.SignAnimationTick(
                        arrayOf("", "Search for", "Server..."),
                    )
                ),
                ConnectorState.ONLINE to listOf(
                    SignData.SignAnimationTick(
                        arrayOf("%service%", "%motd%", "§a§l%state%", "%online_players%/%max_players%"),
                    )
                ),
                ConnectorState.PLAYERS to listOf(
                    SignData.SignAnimationTick(
                        arrayOf("Stopping", "See you soon!"),
                    )
                )
            )
        )
    )
)
