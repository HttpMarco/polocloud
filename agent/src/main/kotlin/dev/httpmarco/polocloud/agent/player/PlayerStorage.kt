package dev.httpmarco.polocloud.agent.player

import dev.httpmarco.polocloud.shared.player.SharedPlayerProvider

interface PlayerStorage : SharedPlayerProvider<AbstractPolocloudPlayer> {

    fun update(player: AbstractPolocloudPlayer)

}