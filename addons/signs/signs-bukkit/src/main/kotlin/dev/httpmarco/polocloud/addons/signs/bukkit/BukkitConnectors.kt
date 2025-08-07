package dev.httpmarco.polocloud.addons.signs.bukkit

import com.google.common.io.ByteStreams
import dev.httpmarco.polocloud.signs.abstraction.Connector
import dev.httpmarco.polocloud.signs.abstraction.Connectors
import dev.httpmarco.polocloud.signs.abstraction.data.banner.BannerData
import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*


object BukkitConnectors : Connectors<Material>() {

    init {
        bindSupport(BukkitSignSupport())
    }

    override fun generateSignConnector(data: SignData) = BukkitSignConnector(data)

    override fun generateBannerConnector(data: BannerData) = BukkitBannerConnector(data)

    override fun connect(uuid: UUID, connector: Connector<*>) {
        val out = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(connector.displayedService!!.name())

        val player = Bukkit.getPlayer(uuid)
        player!!.sendPluginMessage(BukkitBootstrap.plugin, "BungeeCord", out.toByteArray())
    }
}