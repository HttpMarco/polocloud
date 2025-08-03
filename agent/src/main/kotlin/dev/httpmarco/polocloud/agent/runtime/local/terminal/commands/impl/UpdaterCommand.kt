package dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.impl

import dev.httpmarco.polocloud.agent.logger
import dev.httpmarco.polocloud.agent.runtime.local.terminal.arguments.type.KeywordArgument
import dev.httpmarco.polocloud.agent.runtime.local.terminal.commands.Command
import dev.httpmarco.polocloud.common.version.polocloudVersion
import dev.httpmarco.polocloud.updater.Updater

class UpdaterCommand : Command("updater", "Updates the agent to the latest version") {

    init {
        syntax(execution = {
            val versions = Updater.availableRelease()
            val current = polocloudVersion()

            logger.info("Available versions: ${versions.size}")

            if (!versions.contains(current)) {
                logger.info(" &8- &b${current}&8 (&7experimental version&8)")
            }

            versions.forEach {
                logger.info(" &8- &7${it}&8")
            }
        }, KeywordArgument("versions"))
    }
}