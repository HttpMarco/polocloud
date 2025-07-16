package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.CommandService

class HelpCommand(private val commandService: CommandService) : Command("help", "Show all available commands", "?") {

    init {
        defaultExecution {
            logger.info("List of available commands&8:")
            commandService.commands.forEach {
                logger.info(" &8- &f${it.name}&8: &7${it.description}")
            }
        }
    }
}