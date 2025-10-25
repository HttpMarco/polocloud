package dev.httpmarco.polocloud.addons.signs.nukkit

import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.network.protocol.TransferPacket
import com.google.common.io.ByteStreams
import dev.httpmarco.polocloud.signs.abstraction.Connector
import dev.httpmarco.polocloud.signs.abstraction.Connectors
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import java.util.UUID

object NukkitConnectors : Connectors<Block>() {

    init {
        bindSupport(NukkitSignSupport())
    }

    override fun generateSignConnector(data: SignData) = NukkitSignConnector(data)

    override fun generateBannerConnector(data: BannerData): Connector<BannerData.BannerAnimationTick> = NukkitBannerConnector(data)

    override fun connect(uuid: UUID, connector: Connector<*>) {
        val player = Server.getInstance().getPlayer(uuid) ?: return

        val pk = TransferPacket()
        pk.address = connector.displayedService!!.name()
        pk.port = 0

        player.get().dataPacket(pk)
    }
}