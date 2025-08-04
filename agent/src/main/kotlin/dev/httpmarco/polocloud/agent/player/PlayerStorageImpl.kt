package dev.httpmarco.polocloud.agent.player

import dev.httpmarco.polocloud.shared.service.Service
import java.util.concurrent.CompletableFuture

private val cachedPlayers = mutableListOf<AbstractPolocloudPlayer>()

class PlayerStorageImpl : PlayerStorage {
    override fun update(player: AbstractPolocloudPlayer) { // TODO change to events
        val existing = cachedPlayers.indexOfFirst { it.uniqueId == player.uniqueId }
        if (existing >= 0) {
            cachedPlayers[existing] = player
        } else {
            cachedPlayers.add(player)
        }
    }

    override fun findAll(): List<AbstractPolocloudPlayer> {
        return cachedPlayers.toList()
    }

    override fun findAllAsync(): CompletableFuture<List<AbstractPolocloudPlayer>> {
        return CompletableFuture.completedFuture(findAll())
    }

    override fun findByName(name: String): AbstractPolocloudPlayer? {
        return cachedPlayers.find { it.name.equals(name, ignoreCase = true) }
    }

    override fun findByNameAsync(name: String): CompletableFuture<AbstractPolocloudPlayer?> {
        return CompletableFuture.completedFuture(findByName(name))
    }

    override fun findByService(serviceName: String): List<AbstractPolocloudPlayer> {
        return cachedPlayers.filter { it.currentServiceName.equals(serviceName, ignoreCase = true) }
    }

    override fun findByServiceAsync(service: Service): CompletableFuture<List<AbstractPolocloudPlayer>> {
        return CompletableFuture.completedFuture(findByService(service.name()))
    }

    override fun playerCount(): Int {
        return cachedPlayers.size
    }
}