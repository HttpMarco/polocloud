package dev.httpmarco.polocloud.addons.signs.bukkit

import dev.httpmarco.polocloud.addons.api.location.Position
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

class BukkitConnectorEvents : Listener {

    @EventHandler
    fun handleInteract(event: PlayerInteractEvent) {
        val clickedBlock = event.clickedBlock
        if (clickedBlock == null) {
            return
        }

        if (!BukkitConnectors.isSupported(clickedBlock.type)) {
            return
        }

        val connector = BukkitConnectors.find(
            Position(
                clickedBlock.world.name,
                clickedBlock.x.toDouble(),
                clickedBlock.y.toDouble(),
                clickedBlock.z.toDouble()
            )
        )

        if (connector == null || connector.displayedService == null) {
            return
        }

        BukkitConnectors.connect(event.player.uniqueId, connector)
    }

    @EventHandler
    fun blockBreak(event: BlockBreakEvent) {
        val block = event.block
        val connector = BukkitConnectors.find(
            Position(
                block.world.name,
                block.x.toDouble(),
                block.y.toDouble(),
                block.z.toDouble()
            )
        )

        if (connector == null) {
            return
        }
        event.isCancelled = true
    }
}