package dev.httpmarco.polocloud.agent.player

import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider
import java.util.UUID

interface PlayerStorage : SharedPlayerProvider<AbstractPolocloudPlayer> {

    fun addPlayer(player: AbstractPolocloudPlayer)

    fun removePlayer(uniqueId: UUID)

}