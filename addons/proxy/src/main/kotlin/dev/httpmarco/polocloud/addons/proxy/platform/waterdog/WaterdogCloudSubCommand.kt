package dev.httpmarco.polocloud.addons.proxy.platform.waterdog

import dev.waterdog.waterdogpe.command.CommandSender

interface WaterdogCloudSubCommand {
    fun execute(source: CommandSender, args: List<String?>)
}