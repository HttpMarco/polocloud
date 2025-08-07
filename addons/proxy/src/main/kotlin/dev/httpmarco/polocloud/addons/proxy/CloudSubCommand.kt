package dev.httpmarco.polocloud.addons.proxy

import com.velocitypowered.api.command.CommandSource

interface CloudSubCommand {
    fun execute(source: CommandSource, arguments: List<String>)
}