package dev.httpmarco.polocloud.addons.proxy.platform.velocity

import com.velocitypowered.api.command.CommandSource

interface VelocityCloudSubCommand {
    fun execute(source: CommandSource, arguments: List<String>)
}