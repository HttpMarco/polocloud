package dev.httpmarco.polocloud.addons.signs.nukkit

import cn.nukkit.plugin.PluginBase

class NukkitBootstrap: PluginBase() {

    override fun onEnable() {
        NukkitConnectors

        server.commandMap.register("sign", NukkitSignCommand())
        server.pluginManager.registerEvents(NukkitConnectorEvents(), this)
    }
}