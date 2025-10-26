package dev.httpmarco.polocloud.addons.signs.nukkit

import cn.nukkit.block.Block
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.player.PlayerInteractEvent
import dev.httpmarco.polocloud.addons.api.location.Position

class NukkitConnectorEvents: Listener {

    @EventHandler
    fun handleInteract(event: PlayerInteractEvent) {
        val block = event.block ?: return

        if (!NukkitConnectors.isSupported(Block.get(block.id))) {
            return
        }

        val connector = NukkitConnectors.find(
            Position(
                block.level.folderName,
                block.x.toDouble(),
                block.y.toDouble(),
                block.z.toDouble()
            )
        )

        if (connector == null || connector.displayedService == null) {
            return
        }

        NukkitConnectors.connect(event.player.uniqueId, connector)
    }

    @EventHandler
    fun blockBreak(event: BlockBreakEvent) {
        val block = event.block
        val connector = NukkitConnectors.find(
            Position(
                block.level.folderName,
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