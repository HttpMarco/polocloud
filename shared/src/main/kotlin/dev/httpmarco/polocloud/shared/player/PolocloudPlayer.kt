package dev.httpmarco.polocloud.shared.player

import dev.httpmarco.polocloud.v1.player.PlayerSnapshot
import java.util.UUID

open class PolocloudPlayer(
    val name: String,
    val uniqueId: UUID?,
    val currentServiceName: String,
) {

    fun uniqueId(): String = uniqueId.toString()

    companion object {
        fun bindSnapshot(snapshot: PlayerSnapshot): PolocloudPlayer {
            return PolocloudPlayer(
                name = snapshot.name,
                uniqueId = UUID.fromString(snapshot.uniqueId),
                currentServiceName = snapshot.currentServiceName
            )
        }
    }

    fun toSnapshot(): PlayerSnapshot {
        return PlayerSnapshot.newBuilder()
            .setName(name)
            .setUniqueId(uniqueId.toString())
            .setCurrentServiceName(currentServiceName)
            .build()
    }
}