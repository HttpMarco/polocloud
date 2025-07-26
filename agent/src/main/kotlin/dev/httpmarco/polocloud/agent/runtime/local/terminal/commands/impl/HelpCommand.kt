package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.i18n
import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandService

class HelpCommand(private val commandService: CommandService) : Command("help", "Show all available commands", "?") {

    init {
        defaultExecution {
            i18n.info("agent.terminal.command.help.info")
            commandService.commands.forEach {
                logger.info(" &8- &f${it.name}&8: &7${it.description}")
            }
        }
    }
}